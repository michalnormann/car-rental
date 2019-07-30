package com.carrental.gui;

import com.carrental.repository.CarRepo;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Route;

@Route("fotos")
public class FotosGui extends VerticalLayout {

    private CarRepo carRepo;
    public FotosGui(CarRepo carRepo) {
        this.carRepo = carRepo;

        Tab tabview = new Tab();
        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://image.flaticon.com/icons/png/512/38/38967.png", "Aparat Logo");
        img.setHeight("100px");
        appLayout.setBranding(img);

        menu.addMenuItems(new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Add car", "addcar"),
                new AppLayoutMenuItem(VaadinIcon.CAR.create(), "Car list", "list-car"),
                new AppLayoutMenuItem(VaadinIcon.PHONE.create(), "Contact", "contact"),
                new AppLayoutMenuItem(VaadinIcon.CAMERA.create(),"Fotos", "fotos"));

        add(appLayout);
        add(tabview);

    }
}
