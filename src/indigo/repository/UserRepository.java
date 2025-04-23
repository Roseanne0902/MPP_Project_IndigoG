package indigo.repository;

import indigo.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static indigo.Database.getConnection;

public class UserRepository {

  public static boolean authenticateUser(String username, String password) {
    boolean isUserAvailable = false;
    try (Connection connection = getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          "select * from expense_tracker_indigo.user where username = ? and password = ?");
      statement.setString(1, username);
      statement.setString(2, password);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        isUserAvailable = true;
      }
    } catch (SQLException e) {
      System.err.println("Database Error: " + e.getMessage());
    }
    return isUserAvailable;
  }

  public static List<User> getUserByUserId(String username) {
    List<User> users = new ArrayList<>();
    try (Connection connection = getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          "select * from expense_tracker_indigo.user where username = ?");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        User user = new User();
        user.setUserId(resultSet.getString("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setRoleId(resultSet.getString("role_id"));
        users.add(user);
      }
    } catch (SQLException e) {
      System.err.println("Database Error: " + e.getMessage());
    }
    return users;
  }

  public static void createUser(User user) {

    try (Connection connection = getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO expense_tracker_indigo.user (user_id, username, password, email, phone_number, role_id) " +
              "VALUES (?, ?, ?, ?, ?, ?)");

      statement.setString(1, user.getUserId());
      statement.setString(2, user.getUsername());
      statement.setString(3, user.getPassword());
      statement.setString(4, user.getEmail());
      statement.setString(5, user.getPhoneNumber());
      statement.setString(6, user.getRoleId());
      int rows = statement.executeUpdate();
      if (rows > 0) {
        System.out.println("Create user success!");
      }
    } catch (SQLException e) {
      System.err.println("Database Error: " + e.getMessage());
    }
  }

  public static void updateUser(User user) {
    try (Connection connection = getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          "update expense_tracker_indigo.user SET " +
              "username = ?," +
              "password = ?, " +
              "email = ?, " +
              "phone_number = ?," +
              "role_id = ?" +
              "where user_id = ?");
      statement.setString(1, user.getUsername());
      statement.setString(3, user.getPassword());
      statement.setString(3, user.getEmail());
      statement.setString(4, user.getPhoneNumber());
      statement.setString(5, user.getRoleId());
      statement.setString(6, user.getUserId());
      int rows = statement.executeUpdate();
      if (rows > 0) {
        System.out.println("Update successful!");
      }
    } catch (SQLException e) {
      System.err.println("Database Error: " + e.getMessage());
    }
  }

  public static void removeUser(String userId) {
    try (Connection connection = getConnection()) {
      PreparedStatement myStmt = connection.prepareStatement(
          "delete from expense_tracker_indigo.user where user_id = ?");
      myStmt.setString(1, userId);
      int rows = myStmt.executeUpdate();
      if (rows > 0) {
        System.out.println("User removed.");
      }
    } catch (SQLException e) {
      System.err.println("Database Error: " + e.getMessage());
    }
  }

//    public static String hashPassword(String password) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
//            StringBuilder sb = new StringBuilder();
//            for (byte b : hashedBytes) {
//                sb.append(String.format("%02x", b));
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }

  public static String getUserRole(String username) {
    try (Connection connection = getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(
          "SELECT role_id FROM expense_tracker_indigo.user WHERE username = ?"
      );
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getString("role_id");
      }
    } catch (SQLException e) {
      System.err.println("Database Error: " + e.getMessage());
    }
    return null;
  }

}
