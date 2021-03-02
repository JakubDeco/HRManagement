package sk.kosickaakademia.company.database;

import sk.kosickaakademia.company.log.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

    public Connection closeConnection(Connection connection){
        try {
            connection.close();
            Log.print("Connection closed");
        } catch (SQLException e) {
            Log.error(e.toString());
        }
        return null;
    }
}
