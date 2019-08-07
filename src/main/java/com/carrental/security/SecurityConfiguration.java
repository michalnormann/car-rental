package com.carrental.security;

import com.carrental.model.User;
import com.carrental.repository.UserRepo;
import com.carrental.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);

        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("1234"))
                .roles("ADMIN", "USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests()
                .antMatchers("/contact","/","/register").permitAll()
                .antMatchers("/carlist").hasAnyRole("USER", "ADMIN")
                .antMatchers("/addcar").hasRole("ADMIN")
                .and()
                .formLogin().permitAll()
                .and()
                .logout()
                .and()
                .csrf().disable();
    }

    @Autowired
    private UserRepo userRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void get() {
        User admin = new User();
        admin.setLogin("admin");
        admin.setRole("ROLE_ADMIN");
        admin.setPassword(passwordEncoder().encode("1234"));
        userRepo.save(admin);

        User user1 = new User();
        user1.setLogin("user");
        user1.setRole("ROLE_USER");
        user1.setPassword(passwordEncoder().encode("1234"));
        userRepo.save(user1);

        User user2 = new User();
        user2.setLogin("user2");
        user2.setRole("ROLE_USER");
        user2.setPassword(passwordEncoder().encode("1234"));
        userRepo.save(user2);
    }

}
