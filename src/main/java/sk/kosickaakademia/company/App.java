package sk.kosickaakademia.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;


@SpringBootApplication
@ComponentScan(basePackages = "sk.kosickaakademia.company.controller")
public class App {
    public static void main( String[] args ) {
        SpringApplication app = new SpringApplication(App.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", args[0]));
        app.run(args);
//        SpringApplication.run(App.class, args);
    }
}
