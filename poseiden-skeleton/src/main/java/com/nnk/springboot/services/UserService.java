package com.nnk.springboot.services;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DBUser addUser(@Nonnull DBUser DBUserToAdd) {
        DBUserToAdd.setPassword(passwordEncoder.encode(DBUserToAdd.getPassword()));
        userRepository.save(DBUserToAdd);
        return DBUserToAdd;
    }

    public DBUser showUpdateFormForUser(int id) {
        DBUser DBUser = getUserById(id);
        DBUser.setPassword("");
        return DBUser;
    }

    public DBUser getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No user found for Id " + id));
    }

    @Transactional
    public DBUser updateUser(int id, DBUser DBUser) {

        DBUser existingDBUser = getUserById(id);

        existingDBUser.setRole(DBUser.getRole());
        existingDBUser.setFullName(DBUser.getFullName());
        existingDBUser.setUsername(DBUser.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        existingDBUser.setPassword(encoder.encode(DBUser.getPassword()));

        userRepository.save(existingDBUser);

        return existingDBUser;
    }

    @Transactional
    public void deleteUserById(int id) {
        DBUser DBUserToDelete = getUserById(id);
        userRepository.delete(DBUserToDelete);
    }

    public List<DBUser> getAllUsers() {
        return userRepository.findAll();
    }

    public DBUser getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
