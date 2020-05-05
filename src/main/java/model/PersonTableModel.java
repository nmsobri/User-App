package model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.List;

public class PersonTableModel extends AbstractTableModel {

    private String[] columNames = {
            "ID", "Name", "Occupation", "Age",
            "Employment", "Us Citizen", "Tax ID", "Gender"
    };

    private Database database;

    public PersonTableModel(List<Person> persons, Database db) {
        this.database = db;
        this.database.setPersons(persons);
    }

    public void addPerson(Person person) {
        this.database.addPerson(person);
    }

    public void removePerson(int index) {
        this.database.removePerson(index);
    }

    @Override
    public int getRowCount() {
        return this.database.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Person p = this.database.getPerson(rowIndex);

        Object value = null;
        switch (columnIndex) {
            case 0:
                value = p.getID();
                break;
            case 1:
                value = p.getName();
                break;
            case 2:
                value = p.getOccupation();
                break;
            case 3:
                value = p.getAge();
                break;
            case 4:
                value = p.getEmployment();
                break;
            case 5:
                value = p.isUsCitizen();
                break;
            case 6:
                value = p.getTaxID();
                break;
            case 7:
                value = p.getGender();
                break;
        }

        return value;
    }

    @Override
    public String getColumnName(int column) {
        return columNames[column];
    }

    public void saveToFile(File f) throws IOException {
        OutputStream os = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(this.database.getPersons());
    }

    public void loadFromFile(File f) throws IOException {
        InputStream is = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(is);
        this.database.clear();

        try {
            List<Person> persons = (List<Person>) ois.readObject();
            this.database.setPersons(persons);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //Disable editing for id
        if (columnIndex == 0) return false;
        return true;
    }

    @Override
    public void setValueAt(Object val, int rowIndex, int columnIndex) {
        Person p = this.database.getPerson(rowIndex);

        switch (columnIndex) {
            case 0:
                p.setId((int) val);
                break;
            case 1:
                p.setName((String) val);
                break;
            case 2:
                p.setOccupation((String) val);
                break;
            case 3:
                p.setAge((String) val);
                break;
            case 4:
                p.setEmployment((Employee) val);
                break;
            case 5:
                p.setUsCitizen((boolean) val);
                break;
            case 6:
                p.setTaxID((String) val);
                break;
            case 7:
                p.setGender((String) val);
                break;
            default:
                break;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return Employee.class;
            case 5:
                return Boolean.class;
            case 6:
                return String.class;
            case 7:
                return String.class;
            default:
                return null;
        }
    }
}
