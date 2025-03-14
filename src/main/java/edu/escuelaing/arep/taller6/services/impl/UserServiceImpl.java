package edu.escuelaing.arep.taller6.services.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import edu.escuelaing.arep.taller6.exception.UserException;
import edu.escuelaing.arep.taller6.model.User;
import edu.escuelaing.arep.taller6.repository.UserRepository;
import edu.escuelaing.arep.taller6.services.interfaces.UserServices;

@Service
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
    public String hashPasword(String password) throws UserException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new UserException(UserException.SHA_256_ALGORITHM_NOT_FOUND);
        }
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hash));
    }

    @Override
    public List<User> getUsers(){
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User deleteUser(String username) throws UserException {
        User userInDB = getUser(username);
        userRepository.delete(userInDB);
        return userInDB;    
    }

    @Override
    public User getUser(String username) throws UserException {
        Optional<User> userInDB = userRepository.findById(username);
        if(userInDB.isEmpty()){
            throw new UserException(UserException.USER_NOT_FOUND);
        }
        return userInDB.get();
    }
}
