import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by joe on 29/09/2016.
 */
public class Main {

    public static void createTable(Connection conn)throws SQLException{
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users(id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void insertUser(Connection conn, int id, String username, String address, String email)throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES(NULL, ?, ?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.execute();
    }

    public static ArrayList<User> selectUsers(Connection conn)throws SQLException{
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        if(results.next()){
            int id = results.getInt("id");
            String userName= results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            users.add(new User(id, userName, address, email));
        }
        return users;
    }

    public static void updateUser(Connection conn, String userName, String address, String email)throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(" UPDATE users SET address=? WHERE username=?");
        stmt.setString(1, address);
        stmt.setString(2, userName);
        stmt.execute();

        stmt = conn.prepareStatement(" UPDATE users SET email=? WHERE username=?");
        stmt.setString(1, email);
        stmt.setString(2, userName);
        stmt.execute();
    }

    public static void deleteUser(Connection conn, int id)throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id=?");
        stmt.setInt(1,id);
        stmt.execute();
    }

    public static void main(String[] args)throws SQLException {

        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        Spark.externalStaticFileLocation("public");
        Spark.init();

        createTable(conn);

        //takes external folder and points to it's location
        Spark.externalStaticFileLocation("public");
        //start spark engine
        Spark.init();

        //call selectUsers and return data as json
        Spark.get("/user", ((request, response) -> {
            ArrayList<User> users = selectUsers(conn);
            JsonSerializer s = new JsonSerializer();
            return s.serialize(users);
        }));

        Spark.post("/user", ((request, response) -> {

            return "";
        }));
    }
}
