package com.asaitec.fruitstore.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Order {

    private List<OrderLine> orderLines;
    private Float total;
}
