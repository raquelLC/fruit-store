package com.asaitec.fruitstore.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Offer {

    private String fruit;
    private OfferType type;
    private Integer fruitQuantity;
    private Float amountToAplyDiscount;
    private Float discount;
    private String giftedFruit;
    private Integer giftQuantity;
    private Integer fruitDiscountQuantity;
    private String description;
}
