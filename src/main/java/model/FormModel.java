package model;

public class FormModel {
    private static int ID = 0;
    private String name;
    private String occupation;
    private String age;
    private Employee employment;
    private boolean isUsCitizen;
    private String taxID;
    private String gender;

    public static int getID() {
        return ID;
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

    public String getGender() {
        return gender;
    }

    public FormModel(String name, String occupation, String age, Employee employment, boolean isUsCitizen, String taxID, String gender) {
        ID++;
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
