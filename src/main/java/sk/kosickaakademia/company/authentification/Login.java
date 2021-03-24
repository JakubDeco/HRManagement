package sk.kosickaakademia.company.authentification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Login {
    private static Map<String, Date> blocked;
    private static Map<String, Integer> attempt;
    private static Map<String, String> loggedUsers;

    public Login() {
        blocked = new HashMap<>();
        attempt = new HashMap<>();
        loggedUsers  = new HashMap<>();
    }

    public static Map<String, Date> getBlocked() {
        return blocked;
    }

    public static Map<String, Integer> getAttempt() {
        return attempt;
    }

    public static Map<String, String> getLoggedUsers() {
        return loggedUsers;
    }

}
