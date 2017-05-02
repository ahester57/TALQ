package edu.umsl.superclickers.userdata;


public class User extends SimpleUser {

    private String _id = "";
    private String name = "";

    private Stats stats;


    public User(String first, String last, String userId, String email, String unique_id) {
        super(userId, email, first, last);
        this.name = first + " " + last;
        this.stats = new Stats();
        this._id = unique_id;
    }


    public String getName() { return name; }


    public String get_id() { return _id; }


}
