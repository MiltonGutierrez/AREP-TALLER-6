package edu.escuelaing.arep.taller6.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import edu.escuelaing.arep.taller6.exception.UserException;
import edu.escuelaing.arep.taller6.repository.UserRepository;
import edu.escuelaing.arep.taller6.services.impl.UserServiceImpl;

class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        
    }

    @Test
    void testAuthenticateUser() {
        
    }

    @Test
    void testHashPaswordSHA256() throws UserException {
        String password = "password";
        String hashedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
        assertEquals(hashedPassword, userServices.hashPasword(password));
    }
    
}
