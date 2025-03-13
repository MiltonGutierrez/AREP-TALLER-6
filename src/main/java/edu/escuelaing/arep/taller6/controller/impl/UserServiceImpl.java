package edu.escuelaing.arep.taller6.controller.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.escuelaing.arep.taller6.controller.interfaces.UserController;
import edu.escuelaing.arep.taller6.exception.UserException;
import edu.escuelaing.arep.taller6.model.User;
import edu.escuelaing.arep.taller6.services.interfaces.UserServices;

@RestController
@RequestMapping("/user")
public class UserServiceImpl implements UserController {

    private UserServices userService;
    private static final String ERROR_KEY = "error";

    @Autowired
    public UserServiceImpl(UserServices userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping("/")
    public ResponseEntity<Object> createUser(String username, String password) {
        try {
            User userCreated = userService.createUser(username, password);
            return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
        } catch (UserException e) {
             return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(String username, String password) {
        try {
            userService.authenticateUser(username, password);
            return new ResponseEntity<>("Login succesful", HttpStatus.CREATED);
        } catch (UserException e) {
             return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
}
