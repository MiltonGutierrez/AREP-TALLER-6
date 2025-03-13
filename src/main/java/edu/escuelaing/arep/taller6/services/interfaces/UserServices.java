package edu.escuelaing.arep.taller6.services.interfaces;

import java.util.List;

import edu.escuelaing.arep.taller6.exception.UserException;
import edu.escuelaing.arep.taller6.model.User;

public interface UserServices {
    
    User createUser(String username, String password) throws UserException; 
    User getUser(String username) throws UserException;
    User deleteUser(String username) throws UserException;
    void authenticateUser(String username, String password) throws UserException;
    String hashPasword(String password) throws UserException;    
    List<User> getUsers();
}
