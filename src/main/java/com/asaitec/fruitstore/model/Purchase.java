package com.asaitec.fruitstore.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Purchase {

    private String fruit;
    private Integer quantity;
}
