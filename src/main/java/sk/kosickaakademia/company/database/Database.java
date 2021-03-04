package sk.kosickaakademia.company.database;

import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.log.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private String url;
    private String user;
    private String password;
    private final String queryInsertNewUser = "insert into user(fName, lName, age, gender)" +
            " values(?,?,?,?)";

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

    public Connection closeConnection(Connection connection){
        try {
            connection.close();
            Log.print("Connection closed");
        } catch (SQLException e) {
            Log.error(e.toString());
        }
        return null;
    }

    public boolean insertNewUser(User user){
        Connection connection = getConnection();
        if (connection == null) return false;

        try {
            PreparedStatement ps = connection.prepareStatement(queryInsertNewUser);
            ps.setString(1, user.getfName());
            ps.setString(2, user.getlName());
            ps.setInt(3, user.getAge());
            ps.setInt(4, user.getGender().getValue());

            if (ps.executeUpdate() != 0){
                Log.print("New user successfully added to database.");
                return true;
            }
        } catch (SQLException e) {
            Log.error(e.toString());
        }

        return false;
    }
}
