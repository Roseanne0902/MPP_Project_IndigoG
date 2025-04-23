package indigo.repository;

import indigo.model.UserRoleEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static indigo.Database.getConnection;

public class UserRoleRepository {

  public static Map<String, UserRoleEnum> roleList = getRoleList();

  private static Map<String, UserRoleEnum> getRoleList() {
    Map<String, UserRoleEnum> roles = new HashMap<>();
    try (Connection connection = getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          "select * from expense_tracker_indigo.user_role");
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        roles.put(
            resultSet.getString("role_id"),
            UserRoleEnum.valueOf(resultSet.getString("role_name"))
        );
      }
    } catch (SQLException e) {
      System.err.println("Database Error: " + e.getMessage());
    }
    return roles;
  }
}
