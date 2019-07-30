package com.carrental.gui;

import com.carrental.model.Car;
import com.carrental.repository.CarRepo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
@Route("list-car")
public class ListCarGui extends VerticalLayout {

    private CarRepo carRepo;
    
    public ListCarGui(CarRepo carRepo) {
        this.carRepo = carRepo;
        Tab tabview = new Tab();
        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();

//        Image img = new Image("https://i.imgur.com/GPpnszs.png", "Car Rental");
//        img.setHeight("44px");
//        appLayout.setBranding(img);

        menu.addMenuItems(new AppLayoutMenuItem(VaadinIcon.PLUS.create(),"Add car", "addcar"),
                new AppLayoutMenuItem(VaadinIcon.CAR.create(),"Car list", "list-car"),
                new AppLayoutMenuItem(VaadinIcon.PHONE.create(),"Contact", "contact"),
                new AppLayoutMenuItem("Page 4", "page4"));

//        Component content = new Span(new H3("Add new car"),
//                new Span("Page content"));
//        appLayout.setContent(content);

//        Tab carList = new Tab("Car list");
//        Tab login = new Tab("Login");
//        Tab contact = new Tab("Contact");
//        Tabs tabs = new Tabs(carList, login, contact);


        Grid<Car> carGrid = new Grid<>(Car.class);
        carGrid.setItems(carRepo.findAll());

        carGrid.removeColumnByKey("fuel");
        carGrid.removeColumnByKey("yearProduction");
        carGrid.removeColumnByKey("price");

        add(appLayout);
        add(tabview);
        add(carGrid);
    }
}