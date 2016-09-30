import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by joe on 29/09/2016.
 */
public class formTests {

    //helper method
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTable(conn);
        return conn;
    }

    @Test
    public void createUser() {
        User user = new User();

        //test inserting data into user
        user.setId(1);
        user.setUsername("Alice");
        user.setUsername("603 Hermitage Park");
        user.setEmail("hello kitty");
        assertTrue(user != null);
        //need to assert something like user is not null
        //need to test if user variables can be set and are what I expect
    }

    @Test
    public void testInsertUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, 1, "Alice", "603 H", "FFF@FFF");

        ArrayList<User> usersTest = Main.selectUsers(conn);
        conn.close();

        //check for greater than zero, it's more flexible & robust
        assertTrue(usersTest.size() > 0);
    }

    @Test
    public void testUpdateUser() throws SQLException {
        Connection conn = startConnection();
        //need to insert data for database to update
        Main.insertUser(conn, 1, "Alice", "603 H", "FFF@FFF");

        ArrayList<User> usersTest = Main.selectUsers(conn);

        assertTrue(usersTest.size() > 0);

        Main.updateUser(conn, "Alice", "a", "a");

        usersTest = Main.selectUsers(conn);
        conn.close();

        //select here and see what contents are

        for (User user : usersTest) {
            assertTrue(user.username.equals("Alice"));
            assertTrue(user.address.equals("a"));
            assertTrue(user.email.equals("a"));
        }
    }

    @Test
    public void testDeleteUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, 1, "Alice", "603 H", "FFF@FFF");
        ArrayList<User> usersTest = Main.selectUsers(conn);
        assertTrue(usersTest.size() < 0);

        Main.deleteUser(conn, 1);

        usersTest = Main.selectUsers(conn);
        conn.close();

        assertTrue(usersTest.isEmpty());
    }


}
