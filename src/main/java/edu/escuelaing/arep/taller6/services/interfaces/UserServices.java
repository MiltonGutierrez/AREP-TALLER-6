package edu.escuelaing.arep.taller6.services.interfaces;

import edu.escuelaing.arep.taller6.exception.UserException;
import edu.escuelaing.arep.taller6.model.User;

public interface UserServices {
    
    User createUser(String username, String password) throws UserException; 
    void authenticateUser(String username, String password) throws UserException;
    String hashPasword(String password);    
}
