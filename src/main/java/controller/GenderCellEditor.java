package controller;

import model.Gender;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GenderCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JComboBox comboBox;

    public GenderCellEditor() {
        this.comboBox = new JComboBox(Gender.values());
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
