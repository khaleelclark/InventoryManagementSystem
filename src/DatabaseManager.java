package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static Connection connection;

    public static void establishConnection(String userFilePath) {
        String connectionString = "jdbc:sqlite:" + userFilePath;

        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            ProductManager.showError("Connection Error", e.getMessage());
        }
    }


    public static void select() {
        //TODO
    }

    public static void insert() {
        //TODO
    }

    public static void update() {
        //TODO
    }

    public static void delete() {
        //TODO
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            ProductManager.showError("Error Closing Connection", e.getMessage());
        }
    }

}
