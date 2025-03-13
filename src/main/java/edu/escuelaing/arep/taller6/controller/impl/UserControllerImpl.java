package edu.escuelaing.arep.taller6.controller.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.escuelaing.arep.taller6.controller.interfaces.UserController;
import edu.escuelaing.arep.taller6.exception.UserException;
import edu.escuelaing.arep.taller6.model.User;
import edu.escuelaing.arep.taller6.services.interfaces.UserServices;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserControllerImpl implements UserController {

    private UserServices userService;
    private static final String ERROR_KEY = "error";

    @Autowired
    public UserControllerImpl(UserServices userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestParam String username, @RequestParam String password) {
        try {
            User userCreated = userService.createUser(username, password);
            return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
        } catch (UserException e) {
            return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestParam String username, @RequestParam String password) {
        try {
            userService.authenticateUser(username, password);
            return new ResponseEntity<>("Login succesful", HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @GetMapping
    public ResponseEntity<Object> getUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<Object> getUser(String username) {
        try {
            User user = userService.getUser(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable String username) {
        try {
            User user = userService.deleteUser(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
