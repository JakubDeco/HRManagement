package sk.kosickaakademia.company.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sk.kosickaakademia.company.database.Database;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.log.Log;
import sk.kosickaakademia.company.util.Util;

import java.util.List;


@RestController
public class Controller {

    @PostMapping(path = "/user/new")
    public ResponseEntity<String> insertNewUser(@RequestBody String data){
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(data);

            String fName = ((String) jsonObject.get("fName")).trim();
            String lName = ((String) jsonObject.get("lName")).trim();
            int age = Integer.parseInt(String.valueOf(jsonObject.get("age")));
            String gender = (String) jsonObject.get("gender");

            JSONObject response = new JSONObject();
            if (fName.isBlank() || lName.isBlank() || age < 1 || gender.isBlank()){
                Log.error("Incorrect request body.");
                response.put("error", "Incorrect request body.");
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(response.toJSONString());
            }
        } catch (ParseException e) {
            Log.error(e.toString());
        }

        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(null);
        // todo continue
    }

    @GetMapping(path = "/users")
    public ResponseEntity<String> getAllUsers(){
        Database database = new Database();
        List<User> list = database.getAllUsers();
        String response = new Util().getJson(list);

        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
