package com.carrental.Gui;

import com.carrental.model.Car;
import com.carrental.repository.CarRepo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

@Route("")
public class PhotosGui extends VerticalLayout {

    private CarRepo carRepo;

    @Autowired
    public PhotosGui(CarRepo carRepo) {
        this.carRepo = carRepo;

        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://image.flaticon.com/icons/png/512/38/38967.png", "Aparat Logo");
        img.setHeight("100px");
        appLayout.setBranding(img);

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CAR.create(), "Car list", "carlist"));
        }
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CAMERA.create(),"Photos", ""),
                new AppLayoutMenuItem(VaadinIcon.PHONE.create(), "Contact", "contact"));

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Add car", "addcar"));
        }

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Register", "register"));
            AppLayoutMenuItem appLayoutMenuItemLogin = new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Login");
            appLayoutMenuItemLogin.addMenuItemClickListener(menuItemClickEvent ->
            {
                UI.getCurrent().getPage().executeJavaScript("window.open(\"/login\", \"_self\");");
            });
            menu.addMenuItems(appLayoutMenuItemLogin);
        }
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            AppLayoutMenuItem appLayoutMenuItemLogin = new AppLayoutMenuItem(VaadinIcon.EXIT.create(), "Logout");
            appLayoutMenuItemLogin.addMenuItemClickListener(menuItemClickEvent ->
            {
                UI.getCurrent().getPage().executeJavaScript("window.open(\"/logout\", \"_self\");");
            });
            menu.addMenuItems(appLayoutMenuItemLogin);
        }




        List<Car> imageCars = carRepo.findAll();

        VerticalLayout allComponents = new VerticalLayout();
        allComponents.setAlignItems(Alignment.CENTER);
        imageCars.stream().forEach(element -> {
            Image image = new Image(element.getImageURL(),"brak");
            image.setMaxHeight("500px");
            image.setMaxWidth("500px");
            allComponents.add(image);
        });

        add(appLayout);

        appLayout.setContent(allComponents);
        add(appLayout);
    }
}
