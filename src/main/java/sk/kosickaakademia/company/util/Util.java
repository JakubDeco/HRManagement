package sk.kosickaakademia.company.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sk.kosickaakademia.company.entity.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Util {
    public String getJson(List<User> list){
        if (list == null || list.isEmpty())
            return "{}";

        JSONObject root = new JSONObject();
        JSONArray usersArr = new JSONArray();

        for (User user :
                list) {
            if (user != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", user.getId());
                jsonObject.put("fName", user.getfName());
                jsonObject.put("lName", user.getlName());
                jsonObject.put("age", user.getAge());
                jsonObject.put("gender", user.getGender().toString());

                usersArr.add(jsonObject);
            }
        }

        // adding dateTime, users array and count
        root.put("dateTime", Util.getDateTime());
        root.put("users", usersArr);
        root.put("count", usersArr.size());

        return root.toJSONString();
    }

    public String getJson(User user){
        if(user != null){
            JSONObject root = new JSONObject();

            // creating users array key and it's content
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", user.getId());
            jsonObject.put("fName", user.getfName());
            jsonObject.put("lName", user.getlName());
            jsonObject.put("age", user.getAge());
            jsonObject.put("gender", user.getGender().toString());

            JSONArray usersArr = new JSONArray();
            usersArr.add(jsonObject);
            root.put("users", usersArr);

            root.put("count", usersArr.size());

            // adding dateTime
            root.put("dateTime", Util.getDateTime());

            return root.toJSONString();
        }

        return "{}";
    }

    public static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar dateTime = Calendar.getInstance();

        return sdf.format(dateTime.getTime());
    }

    public String normalizeName(String name){
        if (name == null || name.isBlank())
            return "";
        name = name.trim();
        return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }
}