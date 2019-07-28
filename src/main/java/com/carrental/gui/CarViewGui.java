package com.carrental.gui;

import com.carrental.model.CarResponse;
import com.carrental.model.CarType;
import com.carrental.model.Fuel;
import com.carrental.repository.CarRepo;
import com.carrental.service.CarService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@Route("car")
    public class CarViewGui extends VerticalLayout {

    private CarRepo carRepo;
    private CarResponse carResponse;

    @Autowired
    public CarViewGui() {
        this.carRepo = carRepo;

        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://i.imgur.com/GPpnszs.png", "Car Rental");
        img.setHeight("44px");
        appLayout.setBranding(img);

        menu.addMenuItems(new AppLayoutMenuItem(VaadinIcon.PLUS.create(),"Add car", "addcar"),
                new AppLayoutMenuItem(VaadinIcon.CAR.create(),"Car list", "list-car"),
                new AppLayoutMenuItem("Page 3", "page3"),
                new AppLayoutMenuItem("Page 4", "page4"));

        Component content = new Span(new H3("Add new car"),
                new Span("Page content"));
        appLayout.setContent(content);




        TextField markTextField = new TextField("Mark:");
        markTextField.setValue(carResponse.getMark());


/*
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


*/








    }
}
