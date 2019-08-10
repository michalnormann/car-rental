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
        car.setImageURL("http://res.cloudinary.com/dqxizarbt/image/upload/v1565123780/lwy3blg6tyeuflaumkvi.jpg");
        car.setPrice(600);
        carRepo.save(car);

        Car car1 = new Car();
        car1.setModel("Passat");
        car1.setMark("Volkswagen");
        car1.setFuel(Fuel.DIESEL);
        car1.setCarType(CarType.COMBI);
        car1.setYearProduction(2018);
        car1.setImageURL("http://res.cloudinary.com/dqxizarbt/image/upload/v1565200045/uar5srtivzrrachipvud.jpg");
        car1.setPrice(300);
        carRepo.save(car1);

        Car car2 = new Car();
        car2.setModel("Garbus");
        car2.setMark("Volkswagen");
        car2.setFuel(Fuel.GAS);
        car2.setCarType(CarType.HATCHBACK);
        car2.setYearProduction(2000);
        car2.setImageURL("http://res.cloudinary.com/dqxizarbt/image/upload/v1565423677/is8ufk6rexnlrcsacymf.webp");
        car2.setPrice(150);
        carRepo.save(car2);

        Car car3 = new Car();
        car3.setModel("RS5");
        car3.setMark("Audi");
        car3.setFuel(Fuel.GAS);
        car3.setCarType(CarType.HATCHBACK);
        car3.setYearProduction(2015);
        car3.setImageURL("http://res.cloudinary.com/dqxizarbt/image/upload/v1565424403/vcxo7opyaawmeuqyatuz.webp");
        car3.setPrice(400);
        carRepo.save(car3);

        Car car4 = new Car();
        car4.setModel("TT");
        car4.setMark("Audi");
        car4.setFuel(Fuel.GAS);
        car4.setCarType(CarType.HATCHBACK);
        car4.setYearProduction(2018);
        car4.setImageURL("http://res.cloudinary.com/dqxizarbt/image/upload/v1565424562/htguno1iawjviaeuludk.jpg");
        car4.setPrice(400);
        carRepo.save(car4);

        Car car5 = new Car();
        car5.setModel("CLA");
        car5.setMark("Mercedes");
        car5.setFuel(Fuel.DIESEL);
        car5.setCarType(CarType.SEDAN);
        car5.setYearProduction(2018);
        car5.setImageURL("http://res.cloudinary.com/dqxizarbt/image/upload/v1565424119/ic6nwtp7widlj531ydhe.jpg");
        car5.setPrice(400);
        carRepo.save(car5);

        Car car6 = new Car();
        car6.setModel("Golf VII GTI");
        car6.setMark("Volkswagen");
        car6.setFuel(Fuel.GASOLINE);
        car6.setCarType(CarType.HATCHBACK);
        car6.setYearProduction(2018);
        car6.setImageURL("http://res.cloudinary.com/dqxizarbt/image/upload/v1565425616/jzgd1wlhbnhuvexjk1gq.jpg");
        car6.setPrice(300);
        carRepo.save(car6);

        Car car7 = new Car();
        car7.setModel("Wrack Attack v0");
        car7.setMark("Hyundai");
        car7.setFuel(Fuel.GAS);
        car7.setCarType(CarType.COUPE);
        car7.setYearProduction(1981);
        car7.setImageURL("http://res.cloudinary.com/dqxizarbt/image/upload/v1565426025/tw3ehszupl6eznzgco84.jpg");
        car7.setPrice(2);
        carRepo.save(car7);

    }
}