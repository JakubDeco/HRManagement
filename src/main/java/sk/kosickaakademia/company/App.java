package sk.kosickaakademia.company;

import sk.kosickaakademia.company.database.Database;
import sk.kosickaakademia.company.util.Util;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Database database = new Database("src/main/resources/configSchool.properties");
        //database.insertNewUser(new User("Karol", "Kosak", 66, 0));
        Util util = new Util();
        System.out.println(database.getMales());
        System.out.println(database.getUserById(3));
        System.out.println(util.getJson(database.getAllUsers()));
    }
}
