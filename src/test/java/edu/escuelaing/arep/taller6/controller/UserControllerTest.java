package edu.escuelaing.arep.taller6.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
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

        assertEquals(HttpStatus.OK, response.getStatusCode());
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

    @Test
    void testGetUsers_Success() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(userService.getUsers()).thenReturn(mockUsers);

        ResponseEntity<Object> response = userController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        verify(userService, times(1)).getUsers();
    }

    @Test
    void testGetUser_Success() throws UserException {
        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);

        when(userService.getUser(username)).thenReturn(mockUser);

        ResponseEntity<Object> response = userController.getUser(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).getUser(username);
    }

    @Test
    void testGetUser_NotFound() throws UserException {
        String username = "nonExistingUser";
        when(userService.getUser(username)).thenThrow(new UserException(UserException.USER_NOT_FOUND));

        ResponseEntity<Object> response = userController.getUser(username);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Map.of("error", UserException.USER_NOT_FOUND), response.getBody());
    }

    @Test
    void testDeleteUser_Success() throws UserException {
        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);

        when(userService.deleteUser(username)).thenReturn(mockUser);

        ResponseEntity<Object> response = userController.deleteUser(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).deleteUser(username);
    }

    @Test
    void testDeleteUser_NotFound() throws UserException {
        String username = "nonExistingUser";
        when(userService.deleteUser(username)).thenThrow(new UserException(UserException.USER_NOT_FOUND));

        ResponseEntity<Object> response = userController.deleteUser(username);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Map.of("error", UserException.USER_NOT_FOUND), response.getBody());
    }
}
