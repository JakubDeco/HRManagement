package sk.kosickaakademia.company.entity;

import sk.kosickaakademia.company.enumerator.Gender;

public class User {
    private int id;
    private String fName;
    private String lName;
    private int name;
    private Gender gender;

    public User(String fName, String lName, int name, int gender) {
        this.fName = fName;
        this.lName = lName;
        this.name = name;
        this.gender = gender==0 ? Gender.MALE : gender == 1 ? Gender.FEMALE : Gender.OTHER;
    }

    public User(int id, String fName, String lName, int name, int gender) {
        this(fName, lName, name, gender);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public int getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }
}
