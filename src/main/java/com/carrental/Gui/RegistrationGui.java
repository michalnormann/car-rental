package com.carrental.Gui;

import com.carrental.model.User;
import com.carrental.repository.UserRepo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

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

        TextField username = new TextField("Username:");
        PasswordField password1 = new PasswordField("Password:");
        PasswordField password2 = new PasswordField("Confirm password:");
        Div content = new Div();
        content.addClassName("my-style");
        content.setText("The password is not the same, please again!");

        Notification notification = new Notification(content);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(3000);

//// @formatter:off
//        String styles = ".my-style { "
//                + "  color: red;"
//                + " }";
//// @formatter:on
//
//        StreamRegistration resource = UI.getCurrent().getSession()
//                .getResourceRegistry()
//                .registerResource(new StreamResource("styles.css", () -> {
//                    byte[] bytes = styles.getBytes(StandardCharsets.UTF_8);
//                    return new ByteArrayInputStream(bytes);
//                }));
//        UI.getCurrent().getPage().addStyleSheet(
//                "base://" + resource.getResourceUri().toString());

        Button register = new Button("Register", event -> {

            if(password1.getValue().equals(password2.getValue())) {
                User user = new User();
                user.setRole("ROLE_USER");
                user.setLogin(username.getValue());
                user.setPassword(passwordEncoder().encode(password1.getValue()));
                userRepo.save(user);
            } else {
                notification.open();
            }
        });

        add(username,password1,password2,register);

    }
}
