package edu.escuelaing.arep.taller6.exception;

public class UserException extends Exception {

    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String SHA_256_ALGORITHM_NOT_FOUND = "SHA-256 Algorithm not found";
    public static final String USER_NOT_FOUND = "User not found";

    public UserException(String message) {
        super(message);
    }
    
}
