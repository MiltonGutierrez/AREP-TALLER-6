package edu.escuelaing.arep.taller6.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import edu.escuelaing.arep.taller6.exception.UserException;
import edu.escuelaing.arep.taller6.model.User;
import edu.escuelaing.arep.taller6.repository.UserRepository;
import edu.escuelaing.arep.taller6.services.interfaces.UserServices;

public class UserServiceImpl implements UserServices {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String username, String password) throws UserException {
        if (userRepository.findById(username).isPresent()) {
            throw new UserException(UserException.USER_ALREADY_EXISTS);
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashPasword(password));
        return userRepository.save(user);
    }

    @Override
    public void authenticateUser(String username, String password) throws UserException {
        Optional<User> user = userRepository.findById(username);
        if (!user.isPresent() || ! user.get().getPassword().equals(hashPasword(password))) {
            throw new UserException(UserException.INVALID_CREDENTIALS);
        }
    }

    @Override
    public String hashPasword(String password) {
        return password;
    }
    
}
