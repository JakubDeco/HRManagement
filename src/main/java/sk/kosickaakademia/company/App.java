package sk.kosickaakademia.company;

import sk.kosickaakademia.company.database.Database;
import sk.kosickaakademia.company.entity.User;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Database database = new Database("src/main/resources/configSchool.properties");
        database.insertNewUser(new User("Karol", "Kosak", 66, 0));
    }
}
