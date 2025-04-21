package indigo.screen;

import indigo.model.Category;
import indigo.repository.CategoryRepository;
import indigo.util.ButtonEditor;
import indigo.util.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.UUID;

public class AdminDashboard extends JFrame {
    private JTable categoryTable;
    private DefaultTableModel categoryTableModel;
    private JTextField nameField, descField;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        panel.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Manage Categories", createCategoryPanel());
        panel.add(tabs, BorderLayout.CENTER);

        setContentPane(panel);
        loadCategoryData();
    }

    private JPanel createCategoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        categoryTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Update", "Delete"}, 0);
        categoryTable = new JTable(categoryTableModel);
        panel.add(new JScrollPane(categoryTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new FlowLayout());
        nameField = new JTextField(15);
        descField = new JTextField(15);
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        categoryTable.getColumn("Update").setCellRenderer(new ButtonRenderer("Update"));
        categoryTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", e -> {
            int row = categoryTable.getSelectedRow();
            if (row != -1) {
                String id = (String) categoryTableModel.getValueAt(row, 0);
                String name = (String) categoryTableModel.getValueAt(row, 1);
                String desc = (String) categoryTableModel.getValueAt(row, 2);
                nameField.setText(name);
                descField.setText(desc);
            }
        }));

        categoryTable.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));
        categoryTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", e -> {
            int row = categoryTable.getSelectedRow();
            if (row != -1) {
                String id = (String) categoryTableModel.getValueAt(row, 0);
                CategoryRepository.deleteCategory(id);
                loadCategoryData();
            }
        }));

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Description:"));
        form.add(descField);
        form.add(addButton);
        form.add(updateButton);
        form.add(deleteButton);

        addButton.addActionListener((ActionEvent e) -> {
            Category c = new Category(UUID.randomUUID().toString(), nameField.getText(), descField.getText());
            CategoryRepository.addCategory(c);
            loadCategoryData();
        });

        updateButton.addActionListener((ActionEvent e) -> {
            int selected = categoryTable.getSelectedRow();
            if (selected != -1) {
                String id = (String) categoryTableModel.getValueAt(selected, 0);
                Category c = new Category(id, nameField.getText(), descField.getText());
                CategoryRepository.updateCategory(c);
                loadCategoryData();
            }
        });

        deleteButton.addActionListener((ActionEvent e) -> {
            int selected = categoryTable.getSelectedRow();
            if (selected != -1) {
                String id = (String) categoryTableModel.getValueAt(selected, 0);
                CategoryRepository.deleteCategory(id);
                loadCategoryData();
            }
        });

        panel.add(form, BorderLayout.SOUTH);
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