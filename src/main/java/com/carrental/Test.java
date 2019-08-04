package com.carrental;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Fuel;
import com.carrental.model.User;
import com.carrental.repository.CarRepo;
import com.carrental.repository.UserRepo;
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
        car.setRent(true);
        carRepo.save(car);

        Car car1 = new Car();
        car1.setModel("Passat");
        car1.setMark("Volksvagen");
        car1.setFuel(Fuel.DIESEL);
        car1.setCarType(CarType.COMBI);
        car1.setYearProduction(2018);
        car1.setPrice(1000);
        carRepo.save(car1);


    }
}
