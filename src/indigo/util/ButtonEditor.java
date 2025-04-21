package indigo.util;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean clicked;
    private ActionListener action;

    public ButtonEditor(JCheckBox checkBox, String label, ActionListener action) {
        super(checkBox);
        this.label = label;
        this.action = action;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            fireEditingStopped();
            if (action != null) action.actionPerformed(e);
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        button.setText(label);
        clicked = true;
        return button;
    }

    public Object getCellEditorValue() {
        return label;
    }

    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }
}