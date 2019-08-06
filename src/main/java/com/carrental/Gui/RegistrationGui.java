package com.carrental.Gui;

import com.carrental.model.User;
import com.carrental.repository.UserRepo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Route("register")
public class RegistrationGui extends VerticalLayout {

    private UserRepo userRepo;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public RegistrationGui(UserRepo userRepo) {
        this.userRepo = userRepo;

        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://cdn0.iconfinder.com/data/icons/automotive/128/CARS-512.png", "Cars Logo");
        img.setHeight("100px");
        appLayout.setBranding(img);

        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CAR.create(), "Car list", ""),
                new AppLayoutMenuItem(VaadinIcon.PHONE.create(), "Contact", "contact"),
                new AppLayoutMenuItem(VaadinIcon.CAMERA.create(),"Fotos", "fotos"),
                new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Register", "register"));

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {

            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Add car", "addcar"));
        }

        TextField usernameField = new TextField("Username:");
        TextField nameField = new TextField("Name:");
        TextField surnameField = new TextField("Surname:");
        PasswordField password1 = new PasswordField("Password:");
        PasswordField password2 = new PasswordField("Confirm password:");
        Div content = new Div();
        content.addClassName("my-style");
        content.setText("The password is not the same, please again!");
        Div content2 = new Div();
        content2.addClassName("my-style");
        content2.setText("User added successfully.");

        Notification errorNotyfication = new Notification(content);
        errorNotyfication.setPosition(Notification.Position.TOP_CENTER);
        errorNotyfication.setDuration(3000);

        Notification successNotyfication = new Notification(content2);
        successNotyfication.setPosition(Notification.Position.TOP_CENTER);
        successNotyfication.setDuration(3000);


        Button register = new Button("Register", event -> {

            if(password1.getValue().equals(password2.getValue())) {
                User user = new User();
                user.setRole("ROLE_USER");
                user.setLogin(usernameField.getValue());
                user.setName(nameField.getValue());
                user.setSurname(surnameField.getValue());
                user.setPassword(passwordEncoder().encode(password1.getValue()));
                userRepo.save(user);
                successNotyfication.open();
            } else {
                errorNotyfication.open();
            }
        });

        VerticalLayout verticalLayout = new VerticalLayout(usernameField,nameField,surnameField,password1,password2,register);
        Component allComponents = new Span(verticalLayout);
        appLayout.setContent(allComponents);
        add(appLayout);

    }
}
