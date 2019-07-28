package com.carrental.gui;


import com.carrental.repository.CarRepo;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

@Route("main")
public class AddCarGui extends VerticalLayout {

    private CarRepo carRepo;

    public AddCarGui(CarRepo carRepo) {
        this.carRepo = carRepo;

        Tab carList = new Tab("Car list");
        Tab contact = new Tab("Contact");
        Tab logout = new Tab("Log out");
        Tabs tabs = new Tabs(carList, logout, contact);

        

        add(tabs);

    }
}
