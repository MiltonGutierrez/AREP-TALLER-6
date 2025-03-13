package edu.escuelaing.arep.taller6.controller.interfaces;

import org.springframework.http.ResponseEntity;

public interface UserController {

    ResponseEntity<Object> createUser(String username, String password);

    ResponseEntity<Object> loginUser(String username, String password);
}
