package edu.umsl.hester.superclickers;


class User{

    private String firstName;
    private String lastName;
    private int studentId;




    User(String firstName, String lastName, int studentId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;

    }


    String getFirstName() { return firstName; }

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
