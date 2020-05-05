package model;

public enum Gender {
    male("Male"),
    female("Female");

    private String gender;

    private Gender(String gender) {
        this.gender = gender;
    }


    @Override
    public String toString() {
        return this.gender;
    }
}
