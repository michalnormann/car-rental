package com.carrental.gui;

import com.carrental.repository.CarRepo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Route;

@Route("contact")
public class ContactGui extends VerticalLayout {

    private CarRepo carRepo;

    public ContactGui(CarRepo carRepo) {
        this.carRepo = carRepo;

        Tab tabview = new Tab();
        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://cdn4.iconfinder.com/data/icons/rcons-phone/16/handset_round-2-512.png", "Phone Logo");
        img.setHeight("100px");
        appLayout.setBranding(img);

        menu.addMenuItems(new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Add car", "addcar"),
                new AppLayoutMenuItem(VaadinIcon.CAR.create(), "Car list", "list-car"),
                new AppLayoutMenuItem(VaadinIcon.PHONE.create(), "Contact", "contact"),
                new AppLayoutMenuItem(VaadinIcon.CAMERA.create(),"Fotos", "fotos"));

        Component contentNameCompany = new Span(new H3("Rental Car"),
                new Span("Rental-Car Company Information"
                ));
        Component contentPhoneNumber = new Span(new H3("Phone Number"),
                new Span("505-505-505"
                ));
        Component contentAdress = new Span(new H3("Adress"),
                new Span("Al. Jerozolimskie 160   Warszawa , 02-326"
                ));
        VerticalLayout verticalLayout = new VerticalLayout(contentAdress,contentNameCompany,contentPhoneNumber);
        verticalLayout.setSizeFull();
        verticalLayout.setAlignItems(Alignment.CENTER);
        appLayout.setContent(verticalLayout);
        add(appLayout,tabview);


    }
}