package com.carrental.Gui;

import com.carrental.model.User;
import com.carrental.repository.UserRepo;
import com.carrental.service.MailService;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import java.util.Collection;

@Route("register")
public class RegistrationGui extends VerticalLayout {

    private UserRepo userRepo;
    private MailService mailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public RegistrationGui(UserRepo userRepo, MailService mailService) {
        this.userRepo = userRepo;
        this.mailService = mailService;

        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://cdn0.iconfinder.com/data/icons/automotive/128/CARS-512.png", "Cars Logo");
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

        TextField usernameField = new TextField("Username:");
        TextField nameField = new TextField("Name:");
        TextField surnameField = new TextField("Surname:");
        TextField emailField1 = new TextField("E-mail:");
        TextField emailField2 = new TextField("Confirm E-mail:");
        PasswordField password1 = new PasswordField("Password:");
        PasswordField password2 = new PasswordField("Confirm password:");
        Div content = new Div();
        content.addClassName("my-style");
        content.setText("The password or email is not the same, please again!");
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

            if(password1.getValue().equals(password2.getValue()) && emailField1.getValue().equals(emailField2.getValue())) {
                User user = new User();
                user.setRole("ROLE_USER");
                user.setLogin(usernameField.getValue());
                user.setName(nameField.getValue());
                user.setSurname(surnameField.getValue());
                user.setEmail(emailField1.getValue());
                user.setPassword(passwordEncoder().encode(password1.getValue()));
                userRepo.save(user);
                successNotyfication.open();
                if(successNotyfication.isOpened()) {
                    try {
                        mailService.sendMail(user.getEmail(),
                                "Created account in Car Rental",
                                "Hello " + user.getName() + " " + user.getSurname() + ",<br>" +
                                        "your account is created.<br><br>" +
                                        "<b> Username: </b>" + user.getUsername() + "<br>" +
                                        "<b> Password: </b>" + password1.getValue() + "<br><br>" +
                                        "Rent your first car now!"
                                ,
                                true);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
                usernameField.clear();
                nameField.clear();
                surnameField.clear();
                emailField1.clear();
                emailField2.clear();
                password1.clear();
                password2.clear();

            } else {
                errorNotyfication.open();
            }
        });

        HorizontalLayout usernameLayout = new HorizontalLayout(usernameField);
        HorizontalLayout nameAndSurnameLayout = new HorizontalLayout(nameField,surnameField);
        HorizontalLayout emailLayout = new HorizontalLayout(emailField1,emailField2);
        HorizontalLayout passwordLayout = new HorizontalLayout(password1,password2);
        HorizontalLayout submitLayout = new HorizontalLayout(register);

        VerticalLayout verticalLayout = new VerticalLayout(usernameLayout,nameAndSurnameLayout,emailLayout,passwordLayout,submitLayout);
        verticalLayout.setAlignItems(Alignment.CENTER);
        Component allComponents = new Span(verticalLayout);
        appLayout.setContent(allComponents);
        add(appLayout);

    }
}
