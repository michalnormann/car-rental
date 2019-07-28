package com.carrental;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Fuel;
import com.carrental.repository.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Test {

    private CarRepo carRepo;

    @Autowired
    public Test(CarRepo carRepo) {
        this.carRepo = carRepo;

        Car car = new Car();
        car.setModel("Mustang");
        car.setMark("Ford");
        car.setFuel(Fuel.GASOLINE);
        car.setCarType(CarType.SEDAN);
        car.setYearProduction(2019);
        car.setPrice(500);
        carRepo.save(car);
    }
}
