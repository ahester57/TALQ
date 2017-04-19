package edu.umsl.superclickers.userdata;


public class User {

    private String _id = "";
    private String name = "";
    private String email = "";
    private String uid = "";
    private Stats stats;


    public User(String name, String email, String unique_id) {
        this.name = name;
        this.email = email;
        this.uid = unique_id;
        this.stats = new Stats();
        this._id = unique_id;
    }


    public String getName() { return name; }

    public String getEmail() { return email; }

    public String get_id() { return _id; }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + _id + '\'' +
                '}';
    }
}
