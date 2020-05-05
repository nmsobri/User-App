package controller;

import model.Employee;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class EmployeeCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JComboBox comboBox;

    public EmployeeCellEditor() {
        this.comboBox = new JComboBox(Employee.values());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.comboBox.setSelectedItem(value);
        this.comboBox.addActionListener(e -> {
            fireEditingStopped();
        });
        return this.comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        return this.comboBox.getSelectedItem();
    }
}
