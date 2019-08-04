package com.carrental.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CarResponse {

    private String mark;
    private String model;
    private Fuel fuel;
    private double yearProduction;
    private CarType carType;
    private double price;
    private boolean rent = false;
}
