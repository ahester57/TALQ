package edu.umsl.hester.superclickers.userdata;


public class User {

    private String name = "";
    private String email = "";
    private String uid = "";


    public User(String name, String email, String unique_id) {
        this.name = name;
        this.email = email;
        this.uid = unique_id;
    }


    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getUniqueId() { return uid; }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
