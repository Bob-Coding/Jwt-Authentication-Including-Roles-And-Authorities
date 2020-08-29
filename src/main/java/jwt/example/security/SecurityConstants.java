package jwt.example.security;

import jwt.example.ExampleApplicationContext;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000;  //10days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/users";

    //moved the TOKEN constant to application.properties
    public static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) ExampleApplicationContext.getBean("AppProperties");  //method to acces components that were created by springframework
        return appProperties.getTokenSecret();
    }
}