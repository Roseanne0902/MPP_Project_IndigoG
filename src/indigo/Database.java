package indigo;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/expense_tracker_indigo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0942074267Uu+";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }


    public static void queryToDatabase() {
        try (Connection connection = getConnection()) {
            PreparedStatement myStmt = connection.prepareStatement("select * from expense_tracker_indigo.user where username = ?");
            myStmt.setString(1, "thaiphan");
            ResultSet resultSet = myStmt.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("username");
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
    }
}