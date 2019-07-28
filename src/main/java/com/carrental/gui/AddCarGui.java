package com.carrental.gui;


import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Fuel;
import com.carrental.repository.CarRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("addcar")
public class AddCarGui extends VerticalLayout {

    private CarRepo carRepo;

    @Autowired
    public AddCarGui(CarRepo carRepo) {
        this.carRepo = carRepo;

        Tab carList = new Tab("Car list");
        Tab contact = new Tab("Contact");
        Tab logout = new Tab("Log out");
        Tabs tabs = new Tabs(carList, logout, contact);

        TextField markTextField = new TextField("Mark:");
        TextField modelTextField = new TextField("Model:");
        ComboBox<Fuel> fuelComboBox= new ComboBox<>("Fuel",Fuel.values());
        NumberField yearProductionNumberField = new NumberField("Year of Production");
        yearProductionNumberField.setMin(1980);
        yearProductionNumberField.setMax(2019);
        yearProductionNumberField.setErrorMessage("Set year of production from 1980 to 2019.");
        yearProductionNumberField.setHasControls(true);
        ComboBox<CarType> carTypeComboBox= new ComboBox<>("Car Type", CarType.values());
        NumberField priceNumberField = new NumberField("Price per day");
        priceNumberField.setValue(100d);
        priceNumberField.setMin(100);
        priceNumberField.setMax(5000);
        priceNumberField.setHasControls(true);
        Button addButton = new Button("Add new car");
        Label content = new Label(
                "You have added the car correctly.");
        Notification notification = new Notification(content);
        notification.setDuration(2000);
        notification.setPosition(Notification.Position.MIDDLE);
        addButton.addClickListener(event -> {
            Car car = new Car();
            car.setMark(markTextField.getValue());
            car.setModel(modelTextField.getValue());
            car.setFuel(fuelComboBox.getValue());
            car.setYearProduction(yearProductionNumberField.getValue());
            car.setCarType(carTypeComboBox.getValue());
            car.setPrice(priceNumberField.getValue());
            carRepo.save(car);
            notification.open();
            markTextField.setValue("");
            modelTextField.setValue("");
            fuelComboBox.setValue(null);
            yearProductionNumberField.setValue(null);
            carTypeComboBox.setValue(null);
            priceNumberField.setValue(null);
        });


        add(tabs,markTextField,modelTextField,fuelComboBox,yearProductionNumberField,carTypeComboBox,priceNumberField,addButton);

    }
}
