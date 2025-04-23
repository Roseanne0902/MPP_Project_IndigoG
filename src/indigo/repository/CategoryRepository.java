package indigo.repository;

import indigo.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static indigo.Database.getConnection;

public class CategoryRepository {

  public static List<Category> getAllCategories() {
    List<Category> categories = new ArrayList<>();
    try (Connection conn = getConnection()) {
      String  query = "SELECT * FROM expense_tracker_indigo.category";
      PreparedStatement stmt = conn.prepareStatement(query);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Category c = new Category(
            rs.getString("category_id"),
            rs.getString("name"),
            rs.getString("description")
        );
        categories.add(c);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return categories;
  }

  public static String addCategory(Category category) {
    try (Connection conn = getConnection()) {
      String query = "INSERT INTO expense_tracker_indigo.category (category_id, name, description) VALUES (?, ?, ?)";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, category.getCategoryId());
      stmt.setString(2, category.getName());
      stmt.setString(3, category.getDescription());
      stmt.executeUpdate();
      return "Add Category Success!";
    } catch (SQLException e) {
      e.printStackTrace();
      return "Add Category Fail!";
    }
  }

  public static String updateCategory(Category category) {
    try (Connection conn = getConnection()) {
      String query = "UPDATE expense_tracker_indigo.category SET name = ?, description = ? WHERE category_id = ?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, category.getName());
      stmt.setString(2, category.getDescription());
      stmt.setString(3, category.getCategoryId());
      stmt.executeUpdate();
      return "Update Category Success!";
    } catch (SQLException e) {
      e.printStackTrace();
      return "Update Category Fail!";
    }
  }

  public static String deleteCategory(String categoryId) {
    try (Connection conn = getConnection()) {
      String query = "DELETE FROM expense_tracker_indigo.category WHERE category_id = ?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, categoryId);
      stmt.executeUpdate();
      return "Delete Category Success!";
    } catch (SQLException e) {
      e.printStackTrace();
      return "Delete Category Fail!";
    }
  }
}