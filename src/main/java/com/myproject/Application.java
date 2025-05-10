package com.myproject;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@Push
@SpringBootApplication
@Theme(variant = Lumo.DARK)
@CssImport("./styles/shared-styles.css")  // Include any other styles or resources
public class Application implements AppShellConfigurator {

    private static final long serialVersionUID = 1L;

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone(); // You can also use Clock.systemUTC()
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
