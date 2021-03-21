package sk.kosickaakademia.company.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.company.database.Database;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.log.Log;
import sk.kosickaakademia.company.util.Status;
import sk.kosickaakademia.company.util.Util;

import java.util.List;


@RestController
public class Controller {

    @PostMapping(path = "/user/new")
    public ResponseEntity<String> insertNewUser(@RequestBody String data){
        JSONObject response = new JSONObject();
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(data);

            String fName = ((String) jsonObject.get("fName"));
            String lName = ((String) jsonObject.get("lName"));
            int age = Integer.parseInt(String.valueOf(jsonObject.get("age")));
            String gender = (String) jsonObject.get("gender");

            if ( fName.isBlank() || lName.isBlank() || age < 1 ||
                    ( !gender.toLowerCase().equals("male")
                            && !gender.toLowerCase().equals("female")
                            && !gender.toLowerCase().equals("other") ) ){
                Log.error("Incorrect request body.");
                response.put("error", "Incorrect request body.");
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(response.toJSONString());
            }
            int userGender = 0;
            switch (gender.toLowerCase()){
                case "female" -> userGender = 1;
                case "other" -> userGender = 2;
            }

            Database database = new Database();
            if (database.insertNewUser(new User(fName,lName,age,userGender))){
                response.put("info", "New user successfully added.");
                return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(response.toJSONString());
            }
            else {
                response.put("error", "Database error occurred.");
                return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(response.toJSONString());
            }

        } catch (ParseException | NullPointerException e) {
            Log.error("Incorrect request body.");
            response.put("error", "Incorrect request body.");
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(response.toJSONString());
        }
    }

    @GetMapping(path = "/users")
    public ResponseEntity<String> getAllUsers(){
        Database database = new Database();
        List<User> list = database.getAllUsers();
        String response = new Util().getJson(list);

        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping(path = "/user/age")
    public ResponseEntity<String> getUsersByAge(@RequestParam(value = "from") int from, @RequestParam(value = "to") int to){
        Util util = new Util();
        String response = util.getJson(new Database().getUsersByAge(from,to));

        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping(path = "/user/gender/{gender}")
    public ResponseEntity<String> getUsersByGender(@PathVariable String gender){
        if ( !gender.toLowerCase().equals("male")
                && !gender.toLowerCase().equals("female")
                && !gender.toLowerCase().equals("other") ){
            JSONObject response = new JSONObject();
            Log.error("Bad request for getUsersByGender.");
            response.put("error", "Bad request.");
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(response.toJSONString());
        }
        else {
            int userGender = 0;
            switch (gender.toLowerCase()){
                case "female" -> userGender = 1;
                case "other" -> userGender = 2;
            }

            Util util = new Util();
            String response = util.getJson(new Database().getUsersByGender(userGender));

            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
        }
    }

    @GetMapping(path = "/user/id")
    public ResponseEntity<String> getUserByID(@RequestParam(value = "id") int id) {
        Util util = new Util();
        if (id > 0) {
            String response = util.getJson(new Database().getUserById(id));

            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        else {
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body("{}");
        }
    }

    @GetMapping(path = "/user/has")
    public ResponseEntity<String> getUsersWithString(@RequestParam(value = "search") String str){
        Util util = new Util();
        if (str == null || str.isEmpty()) {
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body("{}");
        }
        else {
            String response = util.getJson(new Database().getUsersWithString(str));

            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
        }
    }

    @PutMapping(path = "/user/{id}")
    public ResponseEntity<String> changeUserAge(@PathVariable int id, @RequestBody String body){
        if (id < 1 || body == null || body.isBlank())
            return Status.status400("Bad request.");

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(body);
            int newAge = Integer.parseInt(String.valueOf(jsonObject.get("newAge")));
            if (new Database().changeAge(id, newAge)){
                return Status.status200("User age has been changed.");
            } else {
                return Status.status404("User not found.");
            }
        } catch (ParseException e) {
            return Status.status400("Bad request.");
        }
    }

    @DeleteMapping(path = "/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        if (id < 1)
            return Status.status400("Bad request.");

        if (new Database().deleteUser(id))
            return Status.status200("User deleted.");
        else
            return Status.status404("User not found.");
    }

    @GetMapping(path = "/")
    public ResponseEntity<String> getStatistic(){
        Util util = new Util();
        String response = util.getJson(new Database().getStats());

        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
    }
    //todo methods for each status response entity
}
