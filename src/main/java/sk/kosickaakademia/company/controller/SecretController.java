package sk.kosickaakademia.company.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.company.util.Status;
import sk.kosickaakademia.company.util.Util;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SecretController {
    private final String PASSWORD = "Hollahop";
    private Map<String, String> loggedUsers = new HashMap<>();

    @GetMapping(path = "/secret")
    public ResponseEntity<String> getSecret(@RequestHeader("token") String token){
        String secret = "This is our secret!!!";
        for (Map.Entry<String, String> entry: loggedUsers.entrySet()){
            if (("Bearer "+entry.getValue()).equals(token)) {
                JSONObject response = new JSONObject();
                response.put("secret", secret);
                return Status.status200(response);
            }
        }

        return Status.status401("Invalid token.");
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody String body){

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(body);

            String login = ((String) jsonObject.get("login"));
            String password = ((String) jsonObject.get("password"));

            if (login == null || login.isBlank() || password == null || password.isBlank()){
                return Status.status400("Bad request.");
            }
            if (password.equals(PASSWORD)){
                String token = new Util().generateToken();
                loggedUsers.put(login, token);

                JSONObject response = new JSONObject();
                response.put("info", "User logged in.");
                response.put("login", login);
                response.put("token", "Bearer "+token);

                return Status.status200(response);

            }else {
                return Status.status401("Incorrect password.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Status.status400("Bad request.");
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<String> logout(@RequestHeader("token") String token){
        if (token == null || token.isBlank())
            return Status.status401("Invalid token.");

        for (Map.Entry<String, String> entry: loggedUsers.entrySet()){
            if (("Bearer "+entry.getValue()).equals(token)) {
                loggedUsers.remove(entry.getKey(), entry.getValue());
                return Status.status200("You have been logged out.");
            }
        }

        return Status.status401("Invalid token.");
    }

}
