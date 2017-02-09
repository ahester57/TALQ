package edu.umsl.hester.superclickers.Model;


public class User {

    private String name = "";
    private String email = "";
    private String id = "";


    public User(String name, String email, String unique_id) {
        this.name = name;
        this.email = email;
        this.id = unique_id;
    }


    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getUniqueId() { return id; }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
