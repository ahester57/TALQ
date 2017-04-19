package edu.umsl.superclickers.userdata;


public class User {

    private String _id = "";
    private String name = "";
    private String first = "";
    private String last = "";
    private String userId = "";
    private String email = "";

    private Stats stats;


    public User(String first, String last, String userId, String email, String unique_id) {
        this.first = first;
        this.last = last;
        this.name = first + " " + last;
        this.userId = userId;
        this.email = email;
        this.stats = new Stats();
        this._id = unique_id;
    }

    public String getFirst() { return first; }

    public String getLast() { return last; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getUserId() { return userId; }

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
