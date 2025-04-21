package indigo.model;

import java.util.List;

public class Expense {
    private String expenseId;
    private String userId;
    private String date;
    private double amount;
    private String description;
    private List<String> categoryIds; // many-to-many

    public Expense() {}

    public Expense(String expenseId, String userId, String date, double amount, String description, List<String> categoryIds) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.categoryIds = categoryIds;
    }

    public String getExpenseId() { return expenseId; }
    public void setExpenseId(String expenseId) { this.expenseId = expenseId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<String> categoryIds) { this.categoryIds = categoryIds; }
}
