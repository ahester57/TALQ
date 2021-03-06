package edu.umsl.superclickers.app;

/**
 * Created by Austin on 2/4/2017.
 *
 *
 * I also have a database setup up on but it can
 * be put somewhere more accessable in production.
 *
 * I've setup a database on my server. You can now register;
 *
 *  Test user for log in:
 *     email:   '.'   // literally just dots
 *     pwd:     '.'
 *
 *  Hopefully they will  have a server we can run the php and database on.
 */

public class LoginConfig {
    private static String URL_BASE = "http://tblearn-api.vigilantestudio.com";

    public static String URL_LOGIN = "http://stin.tech/android_login_api/login.php";
    public static String URL_REGISTER = "http://stin.tech/android_login_api/register.php";
    public static String URL_USER_BY_SSO = URL_BASE + "/v1/users/%1$s";

}
