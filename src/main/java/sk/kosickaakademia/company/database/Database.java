package sk.kosickaakademia.company.database;

import sk.kosickaakademia.company.entity.Statistic;
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

    public Database(){
        loadConfig("src/main/resources/configSchool.properties");
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

    public List<User> getUsersByGender(int gender){
        Log.info("Executing Database.getUserByGender");
        Connection connection = getConnection();

        if (gender < 0 || gender > 2)
            return null;

        if (connection != null) {
            String query = "select * from user where gender = ?";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, gender);

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

    public User getUserById(int id){
        Log.info("Executing Database.getUserById");
        Connection connection = getConnection();

        if (id < 1 || connection != null) {
            String query = "select * from user where id = ?";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, id);
                List<User> list = executeSelect(ps);
                User user = null;

                if (!list.isEmpty())
                    user = list.get(0);

                closeConnection(connection);
                return user;
            } catch (SQLException e) {
                Log.error(e.toString());
            }
        }
        return null;
    }

    public boolean changeAge(int id, int age){
        Log.info("Executing Database.changeAge");
        if (id > 0 && age > 0){
            Connection connection = getConnection();
            String query = "update user set age = ? where id = ?";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, age);
                ps.setInt(2, id);

                if (ps.executeUpdate() != 0){
                    closeConnection(connection);
                    return true;
                }
            } catch (SQLException e) {
                Log.error(e.toString());
            }
        }
        return false;
    }

    public List<User> getUsersWithString(String str){
        Log.info("Executing Database.getUsersWithPattern");
        Connection connection = getConnection();

        if (connection != null) {
            String query = "select * from user where fName like ? or lName like ?";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, "%"+str+"%");
                ps.setString(2, "%"+str+"%");
                List<User> list = executeSelect(ps);
                closeConnection(connection);
                return list;
            } catch (SQLException e) {
                Log.error(e.toString());
            }
        }
        return null;
    }

    public Statistic getStats(){
        Log.info("Executing Database.getStats");
        Connection connection = getConnection();

        if (connection != null) {
            String query = "select count(id) as countID," +
                    " sum(if(gender=0, 1, 0)) as male," +
                    " sum(if(gender=1, 1, 0)) as female," +
                    " sum(if(gender=2, 1, 0)) as other," +
                    " avg(age) as ageAvg," +
                    " min(age) as ageMin," +
                    " max(age) as ageMax" +
                    " from user";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                System.out.println(ps);
                ResultSet rs = ps.executeQuery();

                Statistic stats = null;
                if (rs.next()){
                    int count = rs.getInt("countID");
                    int male = rs.getInt("male");
                    int female = rs.getInt("female");
                    int other = rs.getInt("other");
                    double age = rs.getDouble("ageAvg");
                    int min = rs.getInt("ageMin");
                    int max = rs.getInt("ageMax");

                    stats = new Statistic(count, male, female, other, age, min, max);
                }

                closeConnection(connection);
                return stats;
            } catch (SQLException e) {
                Log.error(e.toString());
            }
        }
        return null;
    }

    public boolean deleteUser(int id){
        Log.info("Executing Database.deleteUser");

        if (id < 1)
            return false;

        Connection connection = getConnection();
        if (connection != null){
            String query = "delete from user where id=?";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, id);

                return ps.executeUpdate() != 0;
            } catch (SQLException e) {
                Log.error(e.toString());
            }
        }

        return false;
    }
}
