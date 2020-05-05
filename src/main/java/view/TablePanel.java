package view;

import controller.EmployeeCellEditor;
import model.Database;
import model.Employee;
import model.Person;
import model.PersonTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TablePanel extends JPanel {
    private JTable table;
    private PersonTableModel tableModel;
    private List<Person> persons;
    private JPopupMenu popupMenu;

    public TablePanel(List<Person> persons, Database db) {
        super();

        var dimension = this.getPreferredSize();
        dimension.width = 950;
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);

        this.persons = persons;
        this.tableModel = new PersonTableModel(this.persons, db);

        this.table = new JTable(this.tableModel);
        //Change custom renderer for this column
        this.table.setDefaultRenderer(Employee.class, new EmployeeCellRenderer());

        //Change custom editor for this column
        this.table.setDefaultEditor(Employee.class, new EmployeeCellEditor());
        this.table.setRowHeight(25);

        this.popupMenu = new JPopupMenu();

        JMenuItem deleteRow = new JMenuItem("Delete Row");
        deleteRow.addActionListener((e) -> {
            int row = this.table.getSelectedRow();
            this.tableModel.removePerson(row);
            this.tableModel.fireTableRowsDeleted(row, row);
        });

        popupMenu.add(deleteRow);


        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // Get row at mouse position
                    int row = TablePanel.this.table.rowAtPoint(e.getPoint());
                    // Highlight that row
                    TablePanel.this.table.getSelectionModel().setSelectionInterval(row, row);

                    popupMenu.show(TablePanel.this.table, e.getX(), e.getY());
                }
            }
        });

        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(this.table));
    }

    public void addPerson(Person person) {
        this.tableModel.addPerson(person);
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }

    public void saveToFile(File f) throws IOException {
        this.tableModel.saveToFile(f);
    }

    public void loadFromFile(File f) throws IOException {
        this.tableModel.loadFromFile(f);
    }

    public JTable getTable() {
        return this.table;
    }
}
