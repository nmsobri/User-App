package model;

import java.io.Serializable;

public class Person implements Serializable {
    private int id;
    private String name;
    private String occupation;
    private String age;
    private Employee employment;
    private boolean isUsCitizen;
    private String taxID;
    private Gender gender;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setEmployment(Employee employment) {
        this.employment = employment;
    }

    public void setUsCitizen(boolean usCitizen) {
        isUsCitizen = usCitizen;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getAge() {
        return age;
    }

    public Employee getEmployment() {
        return employment;
    }

    public boolean isUsCitizen() {
        return isUsCitizen;
    }

    public String getTaxID() {
        return taxID;
    }

    public Gender getGender() {
        return gender;
    }

    public Person(int id, String name, String occupation, String age, Employee employment, boolean isUsCitizen, String taxID, Gender gender) {
        this.id = id;
        this.name = name;
        this.occupation = occupation;
        this.age = age;
        this.employment = employment;
        this.isUsCitizen = isUsCitizen;
        this.taxID = taxID;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return this.name + " " + this.occupation + " " + this.age + " " + this.employment +
                " " + this.isUsCitizen + " " + this.taxID + " " + this.gender;
    }
}
