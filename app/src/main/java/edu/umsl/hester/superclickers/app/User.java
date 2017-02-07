package edu.umsl.hester.superclickers.app;


public class User{

    private String name = "";
    private String email = "";
    private int id;


    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    User(String name, int studentId) {
        this.name = name;
        this.id = studentId;

    }


    public String getName() { return name; }

    public String getEmail() { return email; }

    int getStudentId() { return id; }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", studentId=" + id +
                '}';
    }
}
