package model;

public enum Employee {
    un_employed("Unemployed"),
    self_employed("Self employed"),
    private_industry("Private Industry"),
    civil_servant("Civil Servants");

    private String text;

    private Employee(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
