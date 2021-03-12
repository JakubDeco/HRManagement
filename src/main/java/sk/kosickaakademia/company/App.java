package sk.kosickaakademia.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "sk.kosickaakademia.company.controller")
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
        /*Database database = new Database("src/main/resources/configSchool.properties");
        //database.insertNewUser(new User("Karol", "Kosak", 66, 0));
        Util util = new Util();
        System.out.println(database.getMales());
        System.out.println(database.getUserById(3));
        System.out.println(util.getJson(database.getAllUsers()));*/
    }
}
