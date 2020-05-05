package view;

import model.Employee;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class EmployeeCellRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JComboBox<Employee> employmentInput = new JComboBox<>(Employee.values());
        employmentInput.setSelectedItem(value);
        return employmentInput;
    }
}
