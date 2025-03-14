package edu.escuelaing.arep.taller6.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import edu.escuelaing.arep.taller6.exception.UserException;
import edu.escuelaing.arep.taller6.model.User;
import edu.escuelaing.arep.taller6.repository.UserRepository;
import edu.escuelaing.arep.taller6.services.impl.UserServiceImpl;

class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

@Test
    void testCreateUser_Success() throws UserException {
        String username = "testUser";
        String password = "testPass";

        when(userRepository.findById(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser(username, password);

        assertEquals(username, createdUser.getUsername());
        assertNotEquals(password, createdUser.getPassword()); // Assuming hashPassword is used
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        String username = "existingUser";
        when(userRepository.findById(username)).thenReturn(Optional.of(new User()));

        UserException exception = assertThrows(UserException.class, () -> userService.createUser(username, "password"));
        assertEquals(UserException.USER_ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    void testAuthenticateUser_Success() throws UserException {
        String username = "testUser";
        String password = "testPass";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(userService.hashPasword(password));

        when(userRepository.findById(username)).thenReturn(Optional.of(mockUser));

        assertDoesNotThrow(() -> userService.authenticateUser(username, password));
    }

    @Test
    void testAuthenticateUser_Failure_InvalidCredentials() throws UserException {
        String username = "testUser";
        String password = "wrongPass";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(userService.hashPasword("correctPass"));

        when(userRepository.findById(username)).thenReturn(Optional.of(mockUser));

        UserException exception = assertThrows(UserException.class, () -> userService.authenticateUser(username, password));
        assertEquals(UserException.INVALID_CREDENTIALS, exception.getMessage());
    }

    @Test
    void testAuthenticateUser_Failure_UserNotFound() {
        String username = "nonExistingUser";
        when(userRepository.findById(username)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> userService.authenticateUser(username, "password"));
        assertEquals(UserException.INVALID_CREDENTIALS, exception.getMessage());
    }

    @Test
    void testGetUsers() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getUsers();
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUser_Success() throws UserException {
        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);

        when(userRepository.findById(username)).thenReturn(Optional.of(mockUser));
        doNothing().when(userRepository).delete(mockUser);

        User deletedUser = userService.deleteUser(username);

        assertEquals(username, deletedUser.getUsername());
        verify(userRepository, times(1)).delete(mockUser);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        String username = "nonExistingUser";
        when(userRepository.findById(username)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> userService.deleteUser(username));
        assertEquals(UserException.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testGetUser_Success() throws UserException {
        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);

        when(userRepository.findById(username)).thenReturn(Optional.of(mockUser));

        User foundUser = userService.getUser(username);
        assertEquals(username, foundUser.getUsername());
    }

    @Test
    void testGetUser_UserNotFound() {
        String username = "nonExistingUser";
        when(userRepository.findById(username)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> userService.getUser(username));
        assertEquals(UserException.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testHashPaswordSHA256() throws UserException {
        String password = "password";
        String hashedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
        assertEquals(hashedPassword, userService.hashPasword(password));
    }
    
}
