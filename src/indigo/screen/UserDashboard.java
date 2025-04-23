package indigo.screen;

import indigo.model.Category;
import indigo.model.Expense;
import indigo.model.User;
import indigo.repository.CategoryRepository;
import indigo.repository.ExpenseRepository;
import indigo.util.ButtonEditor;
import indigo.util.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDashboard extends JFrame {

  private User currentUser;
  private JTable expenseTable;
  private DefaultTableModel expenseTableModel;

  private JTextField dateField;
  private JTextField amountField;
  private JTextField descriptionField;

  private JList<Category> categoryList;
  private String editingExpenseId = null;

  public UserDashboard(User user) {
    this.currentUser = user;
    setTitle("User Dashboard - " + user.getUsername());
    setSize(900, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel(new BorderLayout());

    JLabel title = new JLabel("Welcome " + user.getUsername(), SwingConstants.CENTER);
    title.setFont(new Font("Serif", Font.BOLD, 26));
    panel.add(title, BorderLayout.NORTH);

    JButton myButton = new JButton("Logout");
    myButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
    myButton.setBounds(230, 220, 120, 40);
    myButton.addActionListener(e -> {
      dispose();
      new LoginScreen().setVisible(true);
    });
    panel.add(myButton, BorderLayout.SOUTH);


    JTabbedPane tabs = new JTabbedPane();
    tabs.add("My Expenses", createExpensePanel());
    tabs.add("Expense Summary", createSummaryPanel());
    tabs.add("Generate Report", createReportGeneratorPanel());

    panel.add(tabs, BorderLayout.CENTER);

    setContentPane(panel);
    loadExpenseData();
  }

  private JPanel createExpensePanel() {
    JPanel panel = new JPanel(new BorderLayout());

    expenseTableModel = new DefaultTableModel(
        new Object[]{"ID", "Date", "Amount", "Categories", "Description", "Update", "Delete"}, 0
    ) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column != 0;
      }
    };

    expenseTable = new JTable(expenseTableModel);
    panel.add(new JScrollPane(expenseTable), BorderLayout.CENTER);

    expenseTable.getColumn("Update").setCellRenderer(new ButtonRenderer("Update"));
    expenseTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", e -> {
      int row = expenseTable.getSelectedRow();
      if (row != -1) {
        dateField.setText((String) expenseTableModel.getValueAt(row, 1));
        amountField.setText(expenseTableModel.getValueAt(row, 2).toString());
        descriptionField.setText((String) expenseTableModel.getValueAt(row, 4));
        editingExpenseId = (String) expenseTableModel.getValueAt(row, 0);

        // Load categories for this expense
        List<String> selectedCatIds =  ExpenseRepository.getCategoriesByExpense(editingExpenseId);
        if (selectedCatIds.isEmpty()) {
          JOptionPane.showMessageDialog(this, "❌ Failed to load categories for editing.");
        }

        ListModel<Category> listModel = categoryList.getModel();
        List<Integer> selectedIndices = new ArrayList<>();

        for (int i = 0; i < listModel.getSize(); i++) {
          if (selectedCatIds.contains(listModel.getElementAt(i).getCategoryId())) {
            selectedIndices.add(i);
          }
        }
        // Set selected categories
        int[] indices = selectedIndices.stream().mapToInt(Integer::intValue).toArray();
        categoryList.setSelectedIndices(indices);
      }
    }));

    expenseTable.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));
    expenseTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", e -> {
      int row = expenseTable.getSelectedRow();
      if (row != -1) {
        String id = (String) expenseTableModel.getValueAt(row, 0);
        String desc = (String) expenseTableModel.getValueAt(row, 4);

        int confirm = JOptionPane.showConfirmDialog(
            UserDashboard.this,
            "Are you sure you want to delete this expense?\n\nDescription: \"" + desc + "\"",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
          String message = ExpenseRepository.deleteExpense(id);
          JOptionPane.showMessageDialog(UserDashboard.this, message);
          loadExpenseData();
        }
      }
    }));

    JPanel form1 = new JPanel(new FlowLayout());
    dateField = new JTextField(8);
    amountField = new JTextField(6);
    descriptionField = new JTextField(15);
    categoryList = new JList<>();
    categoryList.setVisibleRowCount(5);
    categoryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    JScrollPane categoryScroll = new JScrollPane(categoryList);
    categoryScroll.setPreferredSize(new Dimension(150, 50));
    JButton addButton = new JButton("Add");
    JButton updateButton = new JButton("Update");
    JButton deleteButton = new JButton("Delete");

    form1.add(new JLabel("Date (YYYY-MM-DD):"));
    form1.add(dateField);
    form1.add(new JLabel("Amount:"));
    form1.add(amountField);
    form1.add(new JLabel("Categories:"));
    form1.add(categoryScroll);
    form1.add(new JLabel("Description:"));
    form1.add(descriptionField);

    JPanel form2 = new JPanel(new FlowLayout());

    form2.add(addButton);
    form2.add(updateButton);
    form2.add(deleteButton);

    addButton.addActionListener((ActionEvent e) -> {
      List<Category> selectedCategories = categoryList.getSelectedValuesList();
      if (selectedCategories.isEmpty()) return;

      List<String> categoryIds = new ArrayList<>();
      for (Category c : selectedCategories) {
        categoryIds.add(c.getCategoryId());
      }

      Expense expense = new Expense(
          editingExpenseId != null ? editingExpenseId : ExpenseRepository.generateNextExpenseId(),
          currentUser.getUserId(),
          dateField.getText(),
          Double.parseDouble(amountField.getText()),
          descriptionField.getText(),
          categoryIds
      );

      if (editingExpenseId != null) {
        ExpenseRepository.updateExpense(expense);
        JOptionPane.showMessageDialog(this, "✅ Expense updated.");
        editingExpenseId = null;
      } else {
        ExpenseRepository.addExpense(expense);
        JOptionPane.showMessageDialog(this, "✅ Expense added.");
      }

      loadExpenseData();
      clearForm();
    });

    updateButton.addActionListener((ActionEvent e) -> {
      int row = expenseTable.getSelectedRow();
      if (row != -1) {
        dateField.setText((String) expenseTableModel.getValueAt(row, 1));
        amountField.setText(expenseTableModel.getValueAt(row, 2).toString());
        descriptionField.setText((String) expenseTableModel.getValueAt(row, 4));
        editingExpenseId = (String) expenseTableModel.getValueAt(row, 0);

        try {
          List<String> selectedCatIds = ExpenseRepository.getCategoriesByExpense(editingExpenseId);
          ListModel<Category> listModel = categoryList.getModel();
          List<Integer> selectedIndices = new ArrayList<>();
          for (int i = 0; i < listModel.getSize(); i++) {
            if (selectedCatIds.contains(listModel.getElementAt(i).getCategoryId())) {
              selectedIndices.add(i);
            }
          }
          int[] indices = selectedIndices.stream().mapToInt(Integer::intValue).toArray();
          categoryList.setSelectedIndices(indices);
        } catch (Exception ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(this, "❌ Failed to load categories for editing.");
        }
      }
    });

    deleteButton.addActionListener((ActionEvent e) -> {
      int selectedRow = expenseTable.getSelectedRow();
      if (selectedRow != -1) {
        String expenseId = (String) expenseTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this expense?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
          ExpenseRepository.deleteExpense(expenseId);
          loadExpenseData();
          JOptionPane.showMessageDialog(this, "✅ Expense deleted successfully.");
        }
      }
    });

    JPanel panel2 = new JPanel(new BorderLayout());
    panel2.add(form1, BorderLayout.NORTH);
    panel2.add(form2, BorderLayout.SOUTH);
    panel2.setPreferredSize(new Dimension(300, 100));
    panel.add(panel2, BorderLayout.PAGE_END);

    loadCategoriesToList();
    return panel;
  }

  private void clearForm() {
    dateField.setText("");
    amountField.setText("");
    descriptionField.setText("");
    categoryList.clearSelection();
    editingExpenseId = null;
  }


  private void loadCategoriesToList() {
    List<Category> categories = CategoryRepository.getAllCategories();
    DefaultListModel<Category> model = new DefaultListModel<>();
    for (Category c : categories) {
      model.addElement(c);
    }
    categoryList.setModel(model);
  }

  private void loadExpenseData() {
    List<Expense> expenses = ExpenseRepository.getExpensesByUser(currentUser.getUserId());
    expenseTableModel.setRowCount(0);
    for (Expense e : expenses) {
      String categories = String.join(", ", e.getCategoryIds());
      expenseTableModel.addRow(new Object[]{
          e.getExpenseId(),
          e.getDate(),
          e.getAmount(),
          categories,
          e.getDescription(),
          "Update",
          "Delete"
      });
    }
  }

  private JPanel createSummaryPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    JPanel filterPanel = new JPanel(new FlowLayout());
    JTextField fromDateField = new JTextField(10);
    JTextField toDateField = new JTextField(10);
    JButton loadSummaryButton = new JButton("Load Summary");

    filterPanel.add(new JLabel("From (YYYY-MM-DD):"));
    filterPanel.add(fromDateField);
    filterPanel.add(new JLabel("To (YYYY-MM-DD):"));
    filterPanel.add(toDateField);
    filterPanel.add(loadSummaryButton);

    panel.add(filterPanel, BorderLayout.NORTH);

    // Summary Table
    DefaultTableModel summaryModel = new DefaultTableModel(new Object[]{"Category", "Total Amount"}, 0);
    JTable summaryTable = new JTable(summaryModel);
    panel.add(new JScrollPane(summaryTable), BorderLayout.CENTER);

    loadSummaryButton.addActionListener(e -> {
      String fromDate = fromDateField.getText().trim();
      String toDate = toDateField.getText().trim();
      summaryModel.setRowCount(0);

      String query = "SELECT c.name AS category_name, SUM(e.amount) AS total_amount " +
          "FROM expense e " +
          "JOIN expensecategory ec ON e.expense_id = ec.expense_id " +
          "JOIN category c ON ec.category_id = c.category_id " +
          "WHERE e.user_id = ? AND DATE(e.date) BETWEEN ? AND ? " +
          "GROUP BY c.name";

      try (Connection conn = indigo.Database.getConnection();
           PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, currentUser.getUserId());
        stmt.setString(2, fromDate);
        stmt.setString(3, toDate);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          summaryModel.addRow(new Object[]{
              rs.getString("category_name"),
              rs.getDouble("total_amount")
          });
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load summary data.");
      }
    });

    return panel;
  }

  private JPanel createReportGeneratorPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    JPanel form = new JPanel(new FlowLayout());
    JTextField fromField = new JTextField(10);
    JTextField toField = new JTextField(10);
    JComboBox<String> formatBox = new JComboBox<>(new String[]{"PDF", "Excel", "HTML"});
    JButton generateButton = new JButton("Generate Report");

    form.add(new JLabel("From Date (YYYY-MM-DD):"));
    form.add(fromField);
    form.add(new JLabel("To Date (YYYY-MM-DD):"));
    form.add(toField);
    form.add(new JLabel("Format:"));
    form.add(formatBox);
    form.add(generateButton);

    generateButton.addActionListener(e -> {
      String from = fromField.getText().trim();
      String to = toField.getText().trim();
      String format = (String) formatBox.getSelectedItem();

      if (!format.equalsIgnoreCase("PDF")) {
        JOptionPane.showMessageDialog(this, "Only PDF generation is implemented right now.");
        return;
      }

      generatePdfReport(from, to);
    });


    panel.add(form, BorderLayout.NORTH);
    return panel;
  }

  private void generatePdfReport(String from, String to) {
    String fileName = "expense-report-" + currentUser.getUsername() + "-" + from + "-to-" + to + ".pdf";
    try (Connection conn = indigo.Database.getConnection()) {
      String query = "SELECT e.date, e.amount, e.description, GROUP_CONCAT(c.name SEPARATOR ', ') AS categories " +
          "FROM expense e " +
          "JOIN expensecategory ec ON e.expense_id = ec.expense_id " +
          "JOIN category c ON ec.category_id = c.category_id " +
          "WHERE e.user_id = ? AND e.date BETWEEN ? AND ? " +
          "GROUP BY e.expense_id";

      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, currentUser.getUserId());
      stmt.setString(2, from);
      stmt.setString(3, to);

      ResultSet rs = stmt.executeQuery();

      com.lowagie.text.Document document = new com.lowagie.text.Document();
      com.lowagie.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(fileName));
      document.open();

      document.add(new com.lowagie.text.Paragraph("Expense Report for " + currentUser.getUsername()));
      document.add(new com.lowagie.text.Paragraph("From: " + from + "   To: " + to));
      document.add(new com.lowagie.text.Paragraph(" "));

      com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(4);
      table.addCell("Date");
      table.addCell("Amount");
      table.addCell("Categories");
      table.addCell("Description");

      while (rs.next()) {
        table.addCell(rs.getString("date"));
        table.addCell(String.valueOf(rs.getDouble("amount")));
        table.addCell(rs.getString("categories"));
        table.addCell(rs.getString("description"));
      }

      document.add(table);
      document.close();

      JOptionPane.showMessageDialog(this, "PDF saved: " + fileName);
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Failed to generate PDF.");
    }
  }


}