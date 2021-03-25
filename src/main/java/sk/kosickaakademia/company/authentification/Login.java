package sk.kosickaakademia.company.authentification;

import sk.kosickaakademia.company.util.Util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Login {
    private static final String PASSWORD = "Hollahop";
    private static Map<String, Date> blocked;
    private static Map<String, Integer> attempt;
    private static Map<String, String> loggedUsers;

    public Login() {
        blocked = new HashMap<>();
        attempt = new HashMap<>();
        loggedUsers  = new HashMap<>();
    }

    /**
     * This method generates JWT token consisting of 40 characters(upper/lower case letter or integer [0-9])
     * @return String JWT token
     * */
    public String generateToken(){
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            switch (random.nextInt(3)){
                case 0 -> token.append(random.nextInt(10)); // digit
                case 1 -> token.append((char) (random.nextInt(26)+97)); // lowerCase number
                case 2 -> token.append((char) (random.nextInt(26)+65)); // UpperCase number
            }
        }

        return token.toString();
    }

    /**
     * This method blocks user from logging in for 1 minute and removes all invalid password attempts from local memory
     * @param login user login to be blocked
     * */
    public void blockUser(String login){
        Date date = new Date();
        date.setTime(date.getTime()+60000);
        blocked.put(login, date);
        attempt.remove(login);
    }

    /**
     * This method checks whether argument login is blocked or not
     * @param login user login
     * @return boolean true for blocked login false if not blocked
     * */
    public boolean isBlocked(String login){
        Date timeout = blocked.get(login);

        if (timeout == null) {
            return false;
        } else if (timeout.getTime() < new Date().getTime()) {
            blocked.remove(login);
            return false;
        }

        return true;
    }

    /**
     * This method increments number of failed login attempts
     * @param login user login
     * @return int indicating number of failed attempts
     * */
    public int incrementAttempt(String login){
        if (attempt.containsKey(login)) {
            attempt.put(login, attempt.get(login) + 1);
        } else {
            attempt.put(login, 1);
        }

        return attempt.get(login);
    }

    /**
     * This method logs in a user under given login with valid password. However if login is locked "blocked" string
     * is returned. If incorrect password is entered "incorrect" string is returned, failed login attempt is recorded
     * and if count of 3 is reached, user is blocked.
     * @param login user login
     * @param pwd password
     * @return (1) null if either argument is blank or null, (2) "blocked" if login is still blocked, (3) "incorrect"
     * if invalid password was entered or (4) token if none of the above conditions was fulfilled
     * */
    public String login(String login, String pwd){
        if (login == null || login.isBlank()
                || pwd == null || pwd.isBlank() ){
            return null;
        }

        if (isBlocked(login))
            return "blocked";

        if (pwd.equals(PASSWORD)){
            String token = new Util().generateToken();
            loggedUsers.put(login, token);
            return token;
        } else {
            if (incrementAttempt(login) == 3) {
                attempt.remove(login);
                blockUser(login);
            }

            return "incorrect";
        }
    }

}
