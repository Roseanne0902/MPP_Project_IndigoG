package indigo.repository;

import indigo.model.Expense;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static indigo.Database.getConnection;

public class ExpenseRepository {

    public static void addExpense(Expense expense) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // Insert into expense table
            String expenseSql = "INSERT INTO expense (expense_id, user_id, date, amount, description) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(expenseSql);
            stmt.setString(1, expense.getExpenseId());
            stmt.setString(2, expense.getUserId());
            stmt.setString(3, expense.getDate());
            stmt.setDouble(4, expense.getAmount());
            stmt.setString(5, expense.getDescription());
            stmt.executeUpdate();

            // Insert into expensecategory table
            String linkSql = "INSERT INTO expensecategory (expense_id, category_id) VALUES (?, ?)";
            for (String catId : expense.getCategoryIds()) {
                PreparedStatement linkStmt = conn.prepareStatement(linkSql);
                linkStmt.setString(1, expense.getExpenseId());
                linkStmt.setString(2, catId);
                linkStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Expense> getExpensesByUser(String userId) {
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM expense WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String expenseId = rs.getString("expense_id");
                List<String> categoryIds = getCategoriesByExpense(expenseId);
                Expense expense = new Expense(
                        expenseId,
                        userId,
                        rs.getString("date"),
                        rs.getDouble("amount"),
                        rs.getString("description"),
                        categoryIds
                );
                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public static void updateExpense(Expense expense) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // Update main expense fields
            String updateSql = "UPDATE expense SET date = ?, amount = ?, description = ? WHERE expense_id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateSql);
            stmt.setString(1, expense.getDate());
            stmt.setDouble(2, expense.getAmount());
            stmt.setString(3, expense.getDescription());
            stmt.setString(4, expense.getExpenseId());
            stmt.executeUpdate();

            // Clear old categories
            PreparedStatement deleteCats = conn.prepareStatement("DELETE FROM expensecategory WHERE expense_id = ?");
            deleteCats.setString(1, expense.getExpenseId());
            deleteCats.executeUpdate();

            // Insert new categories
            String insertCatSql = "INSERT INTO expensecategory (expense_id, category_id) VALUES (?, ?)";
            for (String catId : expense.getCategoryIds()) {
                PreparedStatement insertStmt = conn.prepareStatement(insertCatSql);
                insertStmt.setString(1, expense.getExpenseId());
                insertStmt.setString(2, catId);
                insertStmt.executeUpdate();
            }
            System.out.println("Updating expense: " + expense.getExpenseId() + " with new amount: " + expense.getAmount());

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteExpense(String expenseId) {
        try (Connection conn = getConnection()) {
            String linkSql = "DELETE FROM expensecategory WHERE expense_id = ?";
            PreparedStatement linkStmt = conn.prepareStatement(linkSql);
            linkStmt.setString(1, expenseId);
            linkStmt.executeUpdate();

            String expSql = "DELETE FROM expense WHERE expense_id = ?";
            PreparedStatement expStmt = conn.prepareStatement(expSql);
            expStmt.setString(1, expenseId);
            expStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getCategoriesByExpense(String expenseId) throws SQLException {
        List<String> categories = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT category_id FROM expensecategory WHERE expense_id = ?");
        stmt.setString(1, expenseId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            categories.add(rs.getString("category_id"));
        }
        return categories;
    }

    public static String generateNextExpenseId() {
        String prefix = "exp";
        int max = 0;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT expense_id FROM expense")) {

            while (rs.next()) {
                String id = rs.getString("expense_id").replace(prefix, "");
                try {
                    int num = Integer.parseInt(id);
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prefix + String.format("%03d", max + 1);
    }
}
