package com.carrental.Gui;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

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



        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CAR.create(), "Car list", ""),
                new AppLayoutMenuItem(VaadinIcon.PHONE.create(), "Contact", "contact"),
                new AppLayoutMenuItem(VaadinIcon.CAMERA.create(), "Photos", "photos"),
                new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Register", "register"));



        Component contentNameCompany = new Span(new H3("Rental Car"),
                new Span("Rental-Car Company Information"
                ));
        Component contentPhoneNumber = new Span(new H3("Phone Number"),
                new Span("505-505-505"
                ));
        Component contentAdress = new Span(new H3("Adress"),
                new Span("Al. Jerozolimskie 160   Warszawa , 02-326"
                ));
        VerticalLayout verticalLayout = new VerticalLayout(contentAdress, contentNameCompany, contentPhoneNumber);
        verticalLayout.setSizeFull();
        verticalLayout.setAlignItems(Alignment.CENTER);
        appLayout.setContent(verticalLayout);

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {

            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Add car", "addcar"));
        }

        add(appLayout, tabview);


    }
}