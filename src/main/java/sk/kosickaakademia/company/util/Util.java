package sk.kosickaakademia.company.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sk.kosickaakademia.company.entity.Statistic;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.log.Log;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

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

    public String getXml(List<User> list){
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("data");
            document.appendChild(root);

            // dateTime element
            Element dateTime = document.createElement("dateTime");
            dateTime.appendChild(document.createTextNode(getDateTime()));
            root.appendChild(dateTime);

            // count element
            Element count = document.createElement("count");
            count.appendChild(document.createTextNode(String.valueOf(list.size())));
            root.appendChild(count);

            // users element
            Element users = document.createElement("users");
            root.appendChild(users);

            for (User temp :
                    list) {
                // user element
                Element user = document.createElement("user");
                users.appendChild(user);

                // id element
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(Integer.toString(temp.getId())));
                user.appendChild(id);

                // fname element
                Element fname = document.createElement("fname");
                fname.appendChild(document.createTextNode(temp.getfName()));
                user.appendChild(fname);

                // lname element
                Element lname = document.createElement("lname");
                lname.appendChild(document.createTextNode(temp.getlName()));
                user.appendChild(lname);

                // age element
                Element age = document.createElement("age");
                age.appendChild(document.createTextNode(Integer.toString(temp.getAge())));
                user.appendChild(age);

                // gender element
                Element gender = document.createElement("gender");
                gender.appendChild(document.createTextNode(temp.getGender().toString()));
                user.appendChild(gender);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            String output = writer.getBuffer().toString().replaceAll("[\n\r]", "");

            return output;
        } catch (ParserConfigurationException | TransformerException e) {
            Log.error(e.toString());
            return null;
        }

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

    public String getJson(Statistic stats){
        JSONObject root = new JSONObject();
        root.put("dateTime", Util.getDateTime());

        if(stats != null){

            // creating users array key and it's content
            JSONObject statsObject = new JSONObject();
            statsObject.put("count", stats.getCount());
            statsObject.put("male", stats.getMale());
            statsObject.put("female", stats.getFemale());
            statsObject.put("other", stats.getOther());
            statsObject.put("averageAge", stats.getAge());
            statsObject.put("minAge", stats.getMin());
            statsObject.put("maxAge", stats.getMax());

            root.put("statistics", statsObject);

            return root.toJSONString();
        }

        return root.toJSONString();
    }

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
}
