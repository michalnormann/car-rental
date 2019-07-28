package com.carrental.service;

import com.carrental.model.Car;
import com.carrental.model.CarResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CarMapper {

    public CarResponse map(Car car) {
        return CarResponse.builder()
                .mark(car.getMark())
                .model(car.getModel())
                .fuel(car.getFuel())
                .yearProduction(car.getYearProduction())
                .carType(car.getCarType())
                .price(car.getPrice())
                .build();
    }

}
