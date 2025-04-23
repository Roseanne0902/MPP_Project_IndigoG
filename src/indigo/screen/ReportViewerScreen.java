package indigo.screen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportViewerScreen extends JFrame {

  private JTable reportTable;
  private DefaultTableModel reportTableModel;
  private JComboBox<String> formatComboBox;
  private JTextField fromDateField;
  private JTextField toDateField;

  public ReportViewerScreen() {
    setTitle("Report Viewer");
    setSize(800, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel(new BorderLayout());

    JLabel header = new JLabel("Report Viewer", SwingConstants.CENTER);
    header.setFont(new Font("Serif", Font.BOLD, 24));
    panel.add(header, BorderLayout.NORTH);

    JPanel filters = new JPanel(new FlowLayout());
    fromDateField = new JTextField(10);
    toDateField = new JTextField(10);
    formatComboBox = new JComboBox<>(new String[]{"All", "PDF", "Excel", "HTML"});
    JButton loadButton = new JButton("Load Reports");
    JButton exportButton = new JButton("Export Selected");

    filters.add(new JLabel("From (YYYY-MM-DD):"));
    filters.add(fromDateField);
    filters.add(new JLabel("To (YYYY-MM-DD):"));
    filters.add(toDateField);
    filters.add(new JLabel("Format:"));
    filters.add(formatComboBox);
    filters.add(loadButton);
    filters.add(exportButton);

    panel.add(filters, BorderLayout.NORTH);

    reportTableModel = new DefaultTableModel(new Object[]{
        "Report ID", "User ID", "Role ID", "Start Date", "End Date", "Format"
    }, 0);

    reportTable = new JTable(reportTableModel);
    panel.add(new JScrollPane(reportTable), BorderLayout.CENTER);

    // Load reports (placeholder logic)
    loadButton.addActionListener(e -> {
      reportTableModel.setRowCount(0);
      reportTableModel.addRow(new Object[]{"rep001", "user001", "role001", "2025-04-01", "2025-04-15", "PDF"});
      reportTableModel.addRow(new Object[]{"rep002", "user002", "role001", "2025-04-01", "2025-04-10", "Excel"});
    });

    // Placeholder for export
    exportButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Exporting selected report..."));

    setContentPane(panel);
  }
}