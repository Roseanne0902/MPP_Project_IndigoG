package indigo.screen;

import indigo.model.Category;
import indigo.model.User;
import indigo.repository.CategoryRepository;
import indigo.util.ButtonEditor;
import indigo.util.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.UUID;

import static javax.swing.JOptionPane.showMessageDialog;

public class AdminDashboard extends JFrame {
  private JTable categoryTable;
  private DefaultTableModel categoryTableModel;

  private JTextField idField;
  private JTextField nameField;
  private JTextField descField;
  private User currentUser;

  public AdminDashboard(User user) {
    this.currentUser = user;
    setTitle("Admin Dashboard");
    setSize(900, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel(new BorderLayout());

    JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
    title.setFont(new Font("Serif", Font.BOLD, 28));
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
//    tabs.add("My Expenses", createExpensePanel());
    tabs.add("Manage Categories", createCategoryPanel());
    panel.add(tabs, BorderLayout.CENTER);

    setContentPane(panel);
    loadCategoryData();
  }

  private JPanel createCategoryPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    categoryTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Update", "Delete"}, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column != 0;
      }
    };

    categoryTable = new JTable(categoryTableModel);

    panel.add(new JScrollPane(categoryTable), BorderLayout.CENTER);

    idField = new JTextField(15);
    nameField = new JTextField(15);
    descField = new JTextField(15);
    JButton addButton = new JButton("Add");

    categoryTable.getColumn("Update").setCellRenderer(new ButtonRenderer("Update"));
    categoryTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", e -> {
      int row = categoryTable.getSelectedRow();
      if (row != -1) {
        String id = (String) categoryTableModel.getValueAt(row, 0);
        String name = (String) categoryTableModel.getValueAt(row, 1);
        String description = (String) categoryTableModel.getValueAt(row, 2);
        String message = CategoryRepository.updateCategory(new Category(id, name, description));
        showMessageDialog(null, message);
        loadCategoryData();
      }
    }));

    categoryTable.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));
    categoryTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", e -> {
      int row = categoryTable.getSelectedRow();
      if (row != -1) {
        String id = (String) categoryTableModel.getValueAt(row, 0);
        String message = CategoryRepository.deleteCategory(id);
        showMessageDialog(null, message);
        loadCategoryData();
      }
    }));

    JPanel form1 = new JPanel(new FlowLayout());
    form1.add(new JLabel("ID:"));
    form1.add(idField);
    form1.add(new JLabel("Name:"));
    form1.add(nameField);
    form1.add(new JLabel("Description:"));
    form1.add(descField);
    JPanel form2 = new JPanel(new FlowLayout());
    form2.add(addButton);

    addButton.addActionListener((ActionEvent e) -> {
      Category c = new Category(idField.getText(), nameField.getText(), descField.getText());
      String result = CategoryRepository.addCategory(c);
      showMessageDialog(null, result);
      loadCategoryData();
    });


    JPanel panel2 = new JPanel();
    panel2.add(form1);
    panel2.add(form2);
    panel2.setPreferredSize(new Dimension(200, 100));

    panel.add(panel2, BorderLayout.PAGE_END);

    return panel;
  }

  private JPanel createSummaryPanel() {
    JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel totalUsersLabel = new JLabel("Total Users: 3");
    JLabel totalCategoriesLabel = new JLabel("Total Categories: 4");
    JLabel totalExpensesLabel = new JLabel("Total Expenses: 4");

    totalUsersLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    totalCategoriesLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    totalExpensesLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

    panel.add(totalUsersLabel);
    panel.add(totalCategoriesLabel);
    panel.add(totalExpensesLabel);

    return panel;
  }

  private void loadCategoryData() {
    List<Category> categories = CategoryRepository.getAllCategories();
    categoryTableModel.setRowCount(0);
    for (Category c : categories) {
      categoryTableModel.addRow(new Object[]{c.getCategoryId(), c.getName(), c.getDescription(), "Update", "Delete"});
    }
  }
}