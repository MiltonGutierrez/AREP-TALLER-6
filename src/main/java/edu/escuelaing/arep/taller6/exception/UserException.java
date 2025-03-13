package edu.escuelaing.arep.taller6.exception;

public class UserException extends Exception {

    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";

    public UserException(String message) {
        super(message);
    }
    
}
