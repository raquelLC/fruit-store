package com.asaitec.fruitstore.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Fruit {

    private String name;
    private Float price;
}
