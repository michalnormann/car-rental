package com.carrental.gui;


import com.carrental.repository.CarRepo;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

@Route("main")
public class CarGui extends VerticalLayout {

    private CarRepo carRepo;

    public CarGui(CarRepo carRepo) {
        this.carRepo = carRepo;

        Tab carList = new Tab("Car list");
        Tab login = new Tab("Login");
        Tab contact = new Tab("Contact");
        Tabs tabs = new Tabs(carList, login, contact);


        add(tabs);

    }
}
