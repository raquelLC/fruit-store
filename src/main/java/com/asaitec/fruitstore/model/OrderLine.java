package com.asaitec.fruitstore.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLine {

    private String fruit;
    private Integer quantity;
    private Float pricePerUnit;
    private Float total;
}
