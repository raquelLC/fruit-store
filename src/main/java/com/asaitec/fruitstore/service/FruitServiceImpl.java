package com.asaitec.fruitstore.service;

import com.asaitec.fruitstore.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * The type Fruit service.
 */
@Service
@RequiredArgsConstructor
public class FruitServiceImpl implements FruitService {

    private final LoadDataComponent loadDataComponent;


    @Override
    public void processPurchase() {

        Map<String, Float> fruitPrices = getFruitPrices();
        Map<String, Integer> groupedShoppingList = getShoppingList();
        Map<String, List<Offer>> activeOffers = getActiveOffers();
        List<OrderLine> orderLines = generateOrderLines(fruitPrices, groupedShoppingList);


        AtomicReference<Float> generalDiscount = new AtomicReference<>(0F);
        Map<String, Integer> gifts = new HashMap<>();
        orderLines.forEach(orderLine -> {
            List<Offer> availableOffers = activeOffers.get(orderLine.getFruit());
            if (availableOffers != null) {
                availableOffers.forEach(offer -> {
                    switch (offer.getType()) {
                        case GENERAL_DISCOUNT: {
                            if (orderLine.getTotal() >= offer.getAmountToAplyDiscount()) {
                                int times = (int) (orderLine.getTotal() / offer.getAmountToAplyDiscount());
                                generalDiscount.updateAndGet(v -> v + times * offer.getDiscount());
                            }
                            break;
                        }
                        case GIFT: {
                            if (orderLine.getQuantity() >= offer.getFruitQuantity()) {
                                int times = orderLine.getQuantity() / offer.getFruitQuantity();
                                if (!gifts.containsKey(orderLine.getFruit())) {
                                    gifts.put(offer.getGiftedFruit(), 0);
                                }
                                gifts.computeIfPresent(offer.getGiftedFruit(), (fruit, quantity) -> quantity + (offer.getGiftQuantity() * times));
                                break;
                            }
                        }
                        case FRUIT_DISCOUNT: {
                            break;
                        }
                    }
                });
            }

        });

        System.out.println("GeneralDiscount: " + generalDiscount.get());
        System.out.println("Gifted fruits: ");
        gifts.forEach((entry, key) -> {
            System.out.println("Gifted " + entry + ": " + key);
        });

        Float totalPriceWithouthDiscount  = orderLines.stream().map(line -> line.getTotal()).reduce(Float::sum).orElse(0F);
        System.out.println("Total order price withoud discounts: " + totalPriceWithouthDiscount);
        System.out.println("Total order price: " + (totalPriceWithouthDiscount - generalDiscount.get()));

    }

    private static List<OrderLine> generateOrderLines(Map<String, Float> fruitPrices, Map<String, Integer> groupedShoppingList) {
        List<OrderLine> orderLines = new ArrayList<>();
        groupedShoppingList.forEach((fruit, quantity) -> {
            OrderLine ol = OrderLine.builder()
                    .fruit(fruit)
                    .quantity(quantity)
                    .pricePerUnit(fruitPrices.get(fruit))
                    .total(quantity * (fruitPrices.get(fruit)))
                    .build();
            orderLines.add(ol);
            System.out.println(ol.toString());
        });
        return orderLines;
    }

    private Map<String, List<Offer>> getActiveOffers() {

        Map<String, List<Offer>> offers = new HashMap<>();
        offers.put("apple", Arrays.asList(Offer.builder()
                .fruit("apple")
                .type(OfferType.FRUIT_DISCOUNT)
                .fruitQuantity(3)
                .fruitDiscountQuantity(1)
                .description("Buy 3 Apples and pay 2.")
                .build()));
        offers.put("pear", Arrays.asList(
                Offer.builder()
                        .fruit("pear")
                        .type(OfferType.GIFT)
                        .fruitQuantity(2)
                        .giftedFruit("orange")
                        .giftQuantity(1)
                        .description("Get a free Orange for every 2 Pears you buy.")
                        .build()
                ,
                Offer.builder()
                        .fruit("pear")
                        .type(OfferType.GENERAL_DISCOUNT)
                        .amountToAplyDiscount(4F)
                        .discount(1F)
                        .description("For every 4 € spent on Pears, we will deduct one euro from your final invoice.")
                        .build()

        ));

        return offers;

    }

    private Map<String, Integer> getShoppingList() {
        Set<Purchase> shoppingList = loadDataComponent.loadPurchase();
        Map<String, Integer> groupedShoppingList = shoppingList.stream().collect(
                groupingBy(purchase -> purchase.getFruit(), Collectors.summingInt(purchase -> purchase.getQuantity())));
        return groupedShoppingList;
    }

    private Map<String, Float> getFruitPrices() {
        Map<String, Float> fruitPrices = new HashMap<>();
        Set<Fruit> fruits = loadDataComponent.loadFruits();
        fruits.forEach(fruit -> {
            if (fruit != null) {
                fruitPrices.put(fruit.getName(), fruit.getPrice());
            }
        });
        return fruitPrices;
    }
}
