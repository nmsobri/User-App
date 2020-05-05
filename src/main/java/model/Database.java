package model;

import view.App;

import javax.swing.*;
import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Database {
    private LinkedList<Person> persons;
    private Connection connection;

    private String user;
    private String password;
    private int port;

    public Database(String user, String password, int port) {
        this.persons = new LinkedList<>();
        this.user = user;
        this.password = password;
        this.port = port;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws Exception {
        if (this.connection != null) {
            return;
        }

        var url = String.format("jdbc:mysql://localhost:%d/swing?useSSL=false&useUnicode=true", this.port);
        this.connection = DriverManager.getConnection(url, this.user, this.password);
        System.out.println("Connected to: " + this.connection);
    }

    public void configure(String user, String password, int port) {
        this.user = user;
        this.password = password;
        this.port = port;

        try {
            this.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.connect();
        } catch (Exception e) {
            System.out.println("Cannot make connection " + e.getMessage());
        }
    }

    public void disconnect() throws Exception {
        if (this.connection != null) {
            System.out.println("Disconnected from: " + this.connection);
            this.connection.close();
            this.connection = null;
        }
    }

    public void setPersons(List<Person> p) {
        this.persons.addAll(p);
    }

    public void addPerson(Person p) {
        this.persons.add(p);
    }

    public void removePerson(int index) {
        this.persons.remove(index);
    }

    public List<Person> getPersons() {
        return Collections.unmodifiableList(this.persons);
    }

    public Person getPerson(int index) {
        return this.persons.get(index);
    }

    public void clear() {
        this.persons.clear();
    }

    public int size() {
        return this.persons.size();
    }

    public void load() {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = stmt.executeQuery();
            this.persons.clear();

            while ((resultSet.next())) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String occupation = resultSet.getString(3);
                String age = resultSet.getString(4);
                Employee employment = Enum.valueOf(Employee.class, resultSet.getString(5));
                boolean isUsCitizen = resultSet.getBoolean(6);
                String taxID = resultSet.getString(7);
                String gender = resultSet.getString(8);

                Person p = new Person(id, name, occupation, age, employment, isUsCitizen, taxID, gender);
                this.persons.add(p);
            }

            System.out.println(this.persons);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            PreparedStatement checkStatement = this.connection.prepareStatement("SELECT COUNT(*) FROM users WHERE id = ?");

            for (Person p : this.persons) {
                checkStatement.setInt(1, p.getID());
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();

                int count = resultSet.getInt(1);
                System.out.println(count);

                if (count == 1) {
                    PreparedStatement updateStatement = this.connection.prepareStatement("UPDATE users SET name=?, occupation=?, age=?, employment=?, is_us_citizen=?, tax_id=?, gender=? WHERE id=?");

                    updateStatement.setString(1, p.getName());
                    updateStatement.setString(2, p.getOccupation());
                    updateStatement.setString(3, p.getAge());
                    updateStatement.setString(4, p.getEmployment().name());
                    updateStatement.setBoolean(5, p.isUsCitizen());
                    updateStatement.setString(6, p.getTaxID());
                    updateStatement.setString(7, p.getGender());
                    updateStatement.setInt(8, p.getID());

                    updateStatement.execute();
                } else {
                    PreparedStatement insertStatement = this.connection.prepareStatement("INSERT INTO users (name, occupation, age, employment, is_us_citizen, tax_id, gender) VALUES (?,?,?,?,?,?,?)");

                    insertStatement.setString(1, p.getName());
                    insertStatement.setString(2, p.getOccupation());
                    insertStatement.setString(3, p.getAge());
                    insertStatement.setString(4, p.getEmployment().name());
                    insertStatement.setBoolean(5, p.isUsCitizen());
                    insertStatement.setString(6, p.getTaxID());
                    insertStatement.setString(7, p.getGender());

                    insertStatement.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
