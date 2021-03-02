package sk.kosickaakademia.company.entity;

public class User {
    private int id;
    private String fName;
    private String lName;
    private int name;
    private boolean gender;

    public User(int id, String fName, String lName, int name, boolean gender) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.name = name;
        this.gender = gender;
    }

    public User(String fName, String lName, int name, boolean gender) {
        this.fName = fName;
        this.lName = lName;
        this.name = name;
        this.gender = gender;
    }
}
