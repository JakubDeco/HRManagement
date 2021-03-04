package sk.kosickaakademia.company.database;

import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.log.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {
    private String url;
    private String user;
    private String password;

    public Database(String filepath){
        loadConfig(filepath);
    }

    private void loadConfig(String filepath){
        try {
            InputStream inputStream = new FileInputStream(filepath);

            Properties properties=new Properties();

            properties.load(inputStream);

            url=properties.getProperty("url");
            user=properties.getProperty("user");
            password=properties.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url,user,password);
            Log.print("Connection successful");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            Log.error(e.toString());
        }
        return null;
    }

    public void closeConnection(Connection connection){
        try {
            connection.close();
            Log.print("Connection closed");
        } catch (SQLException e) {
            Log.error(e.toString());
        }
    }

    public boolean insertNewUser(User user){
        Log.info("Executing Database.insertNewUser");

        Connection connection = getConnection();
        if (connection == null) return false;

        try {
            String queryInsertNewUser = "insert into user(fName, lName, age, gender)" +
                    " values(?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(queryInsertNewUser);
            ps.setString(1, user.getfName());
            ps.setString(2, user.getlName());
            ps.setInt(3, user.getAge());
            ps.setInt(4, user.getGender().getValue());

            if (ps.executeUpdate() != 0){
                Log.print("New user successfully added to database.");
                closeConnection(connection);
                return true;
            }
        } catch (SQLException e) {
            Log.error(e.toString());
        }

        closeConnection(connection);
        return false;
    }

    public List<User> getFemales(){
        Log.info("Executing Database.getFemales");
        Connection connection = getConnection();

        if (connection != null) {
            String query = "select * from user where gender = 1";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                List<User> list = executeSelect(ps);
                closeConnection(connection);
                return list;
            } catch (SQLException e) {
                Log.error(e.toString());
            }
        }
        return null;
    }

    public List<User> getMales(){
        Log.info("Executing Database.getMales");
        Connection connection = getConnection();

        if (connection != null) {
            String query = "select * from user where gender = 0";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                List<User> list = executeSelect(ps);
                closeConnection(connection);
                return list;
            } catch (SQLException e) {
                Log.error(e.toString());
            }
        }
        return null;
    }

    private List<User> executeSelect(PreparedStatement ps) {
        List<User> list = new ArrayList<>();
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String fName = rs.getString("fName");
                String lName = rs.getString("lName");
                int age = rs.getInt("age");
                int gender = rs.getInt("gender");

                list.add(new User(id, fName, lName, age, gender));
            }
        } catch (SQLException e) {
            Log.error(e.toString());
        }
        return list;
    }

    public List<User> getUsersByAge(int from, int to){
        Log.info("Executing Database.getUsersByAge");

        if (from > 0 && to > 0 && from <=to && to < 110){

            Connection connection = getConnection();
            if (connection != null){

                String query = "select * from user where age between ? and ?";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, from);
                    ps.setInt(2, to);

                    List<User> list = executeSelect(ps);
                    closeConnection(connection);

                    return list;
                } catch (SQLException e) {
                    Log.error(e.toString());
                }
            }

        }
        return null;
    }

    public List<User> getAllUsers(){
        Log.info("Executing Database.getAllUsers");
        Connection connection = getConnection();

        if (connection != null) {
            String query = "select * from user";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                List<User> list = executeSelect(ps);
                closeConnection(connection);
                return list;
            } catch (SQLException e) {
                Log.error(e.toString());
            }
        }
        return null;
    }
    // getUser(id)
}
