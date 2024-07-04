package com.nnk.springboot.services;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DBUser addUser(@Nonnull DBUser DBUserToAdd) {
        if (isUserNameAlreadyUsed(DBUserToAdd.getUsername())) {
            throw new IllegalArgumentException("userName already used");
        }
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
    public DBUser updateUser(int id, DBUser dbUser) {

        DBUser existingDBUser = getUserById(id);


        if (!existingDBUser.getUsername().equals(dbUser.getUsername()) && isUserNameAlreadyUsed(dbUser.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        existingDBUser.setRole(dbUser.getRole());
        existingDBUser.setFullName(dbUser.getFullName());
        existingDBUser.setUsername(dbUser.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        existingDBUser.setPassword(encoder.encode(dbUser.getPassword()));

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

    public boolean isUserNameAlreadyUsed(String userNameToCheck) {
        Optional<DBUser> optionalUser = Optional.ofNullable(userRepository.findByUsername(userNameToCheck));
        return optionalUser.isPresent();
    }
}
