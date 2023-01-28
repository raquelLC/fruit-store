package com.asaitec.fruitstore.service;

import com.asaitec.fruitstore.FruitStoreApplication;
import com.asaitec.fruitstore.model.Fruit;
import com.asaitec.fruitstore.model.Purchase;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Load data component.
 */
@Log4j2
@Component
public class LoadDataComponent {

    /**
     * The constant PROPERTY_SEPARATOR.
     */
    public static final String PROPERTY_SEPARATOR = ",";

    /**
     * The Fruit resource.
     */
    @Value("classpath:fruit.txt")
    Resource fruitResource;
    /**
     * The Purchase resource.
     */
    @Value("classpath:purchase.txt")
    Resource purchaseResource;

    /**
     * Load fruits set.
     *
     * @return the set
     */
    public Set<Fruit> loadFruits() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(fruitResource.getInputStream());
            Set<Fruit> fruitsInFile = new BufferedReader(inputStreamReader)
                    .lines()
                    .filter(line -> !StringUtils.isEmpty(line))
                    .map(f -> convertToFruit(f))
                    .collect(Collectors.toSet());
            return fruitsInFile;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error reading file",
                    e
            );
        }
    }

    public Set<Purchase> loadPurchase() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(purchaseResource.getInputStream());
            Set<Purchase> fruitsInFile = new BufferedReader(inputStreamReader)
                    .lines()
                    .filter(line -> !StringUtils.isEmpty(line))
                    .map(f -> convertToPurchase(f))
                    .collect(Collectors.toSet());
            return fruitsInFile;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error reading file",
                    e
            );
        }
    }

    private Fruit convertToFruit(String fruitString) {

        String[] splitted = fruitString.split(PROPERTY_SEPARATOR);
        if (splitted.length >= 2) {
            return Fruit.builder()
                    .name(splitted[0].trim().toLowerCase())
                    .price(Float.valueOf(splitted[1].trim()))
                    .build();
        } else {
            log.error("Wrong format in fruit file");
        }
        return null;
    }

    private Purchase convertToPurchase(String purchaseString) {

        String[] splitted = purchaseString.split(PROPERTY_SEPARATOR);
        if (splitted.length >= 2) {
            return Purchase.builder()
                    .fruit(splitted[0].trim().toLowerCase())
                    .quantity(Integer.valueOf(splitted[1].trim()))
                    .build();
        } else {
            log.error("Wrong format in purchase file");
        }
        return null;
    }

}
