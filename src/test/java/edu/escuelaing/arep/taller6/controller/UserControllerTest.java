package edu.escuelaing.arep.taller6.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.escuelaing.arep.taller6.controller.impl.UserControllerImpl;
import edu.escuelaing.arep.taller6.exception.UserException;
import edu.escuelaing.arep.taller6.model.User;
import edu.escuelaing.arep.taller6.services.interfaces.UserServices;

class UserControllerTest {

    @Mock
    private UserServices userService;

    @InjectMocks
    UserControllerImpl userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccess() throws UserException {
        String username = "testUser";
        String password = "testPass";
        
        User mockUser = new User(username, userService.hashPasword(password));

        when(userService.createUser(username, password)).thenReturn(mockUser);

        ResponseEntity<Object> response = userController.createUser(username, password);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void testCreateUserFailure() throws UserException {
        String username = "testUser";
        String password = "testPass";
        String errorMessage = UserException.INVALID_CREDENTIALS;

        when(userService.createUser(username, password)).thenThrow(new UserException(errorMessage));

        ResponseEntity<Object> response = userController.createUser(username, password);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        assertEquals(errorMessage, ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void testLoginUserSuccess() throws UserException {
        String username = "testUser";
        String password = "testPass";

        doNothing().when(userService).authenticateUser(username, password);

        ResponseEntity<Object> response = userController.loginUser(username, password);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Login succesful", response.getBody());
    }

    @Test
    void testLoginUserFailure() throws UserException {
        String username = "testUser";
        String password = "testPass";
        String errorMessage = UserException.INVALID_CREDENTIALS;
        doThrow(new UserException(errorMessage)).when(userService).authenticateUser(username, password);

        ResponseEntity<Object> response = userController.loginUser(username, password);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        assertEquals(errorMessage, ((Map<?, ?>) response.getBody()).get("error"));
    }
}
