package edu.umsl.hester.superclickers.app;

/**
 * Created by Austin on 2/4/2017.
 *
 * This requires having a server running on your local network.
 * I have WampServer running on my machine with some php scripts
 * to handle registering and logging in.
 *
 * I also have a database setup up on my local machine, but it can
 * be put somewhere more accessable in production.
 *
 * I've setup a database on my server. You can now register;
 *
 *  Test user for log in:
 *      fart@johnson.com
 *      fart
 *
 *  Hopefully they will  have a server we can run the php and database on.
 */

public class LoginConfig {

    public static String URL_LOGIN = "http://stin.tech/android_login_api/login.php";

    public static String URL_REGISTER = "http://stin.tech/android_login_api/register.php";

    public static String URL_CREATE_GROUP = "http://stin.tech/android_login_api/create_group.php";

    public static String URL_JOIN_GROUP = "http://stin.tech/android_login_api/join_group.php";
}
