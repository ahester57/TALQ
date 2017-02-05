package edu.umsl.hester.superclickers.app;


public class User{

    private String firstName = "", lastName = "";
    private int studentId;


    public User(String name) {
        this.firstName = name;
    }

    User(String firstName, String lastName, int studentId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;

    }


    public String getFirstName() { return firstName; }

    String getLastName() { return lastName; }

    int getStudentId() { return studentId; }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", studentId=" + studentId +
                '}';
    }
}
