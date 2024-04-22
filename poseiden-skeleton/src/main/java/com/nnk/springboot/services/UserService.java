package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User addUser(User userToAdd) {
        userToAdd.setPassword(passwordEncoder.encode(userToAdd.getPassword()));
        userRepository.save(userToAdd);
        return userToAdd;
    }

    public User showUpdateFormForUser(int id) {
        User user =  userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No user found for Id " + id));
        user.setPassword("");
        return user;
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No user found for Id " + id));
    }

    public User updateUser(int id, User user) {

        User existingUser =  getUserById(id);

        existingUser.setRole(user.getRole());
        existingUser.setFullName(user.getFullName());
        existingUser.setUsername(user.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        existingUser.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(existingUser);

        return existingUser;
    }

    public void deleteUserById(int id) {
        User userToDelete = getUserById(id);
        userRepository.delete(userToDelete);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
