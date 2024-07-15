package com.nnk.springboot.services;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.domain.DTO.DBUserDTO;
import com.nnk.springboot.domain.parameter.DBUserParameter;
import com.nnk.springboot.exceptions.UserNameAlreadyUsedException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.util.DBUserMapper;
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

    @Autowired
    private DBUserMapper dbUserMapper;

    @Transactional
    public DBUser addUser(@Nonnull DBUserParameter dbUserParameter) {

        // Check if the userName is already used by another user, we want to maintain uniqueness
        // since our CustomUserDetailsService loads users by their userName to create userDetails objects.
        if (isUserNameAlreadyUsed(dbUserParameter.getUsername())) {
            throw new UserNameAlreadyUsedException("userName already used");
        }
        DBUser dbUserToAdd = dbUserMapper.toDBUser(dbUserParameter);
        dbUserToAdd.setPassword(passwordEncoder.encode(dbUserToAdd.getPassword()));
        userRepository.save(dbUserToAdd);
        return dbUserToAdd;
    }

    public DBUserParameter showUpdateFormForUser(int id) {
        DBUser dbUser = getUserById(id);
        dbUser.setPassword("");
        return dbUserMapper.toDBUserParameter(dbUser);
    }

    public DBUser getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No user found for Id " + id));
    }

    @Transactional
    public DBUser updateUser(int id, DBUserParameter dbUserParameter) {

        DBUser existingDBUser = getUserById(dbUserParameter.getId());

        // Check if the userName field of the dbUserParameter object is different from the DBUser object we are trying to update's userName field.
        // If they are the same, we still want to set the other fields that might have been modified by the user on form submission and retain
        // unique usernames while persisting the entity.

        if (!existingDBUser.getUsername().equals(dbUserParameter.getUsername()) && isUserNameAlreadyUsed(dbUserParameter.getUsername())) {
            throw new UserNameAlreadyUsedException("Username already exists");
        }

        existingDBUser.setRole(dbUserParameter.getRole());
        existingDBUser.setFullName(dbUserParameter.getFullName());
        existingDBUser.setUsername(dbUserParameter.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        existingDBUser.setPassword(encoder.encode(dbUserParameter.getPassword()));

        userRepository.save(existingDBUser);

        return existingDBUser;
    }

    @Transactional
    public void deleteUserById(int id) {
        DBUser dbUserToDelete = getUserById(id);
        userRepository.delete(dbUserToDelete);
    }

    public List<DBUserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(dbUserMapper::toDBuserDTO)
                .toList();
    }

    public DBUser getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Boolean method to check if a userName is already used
    public boolean isUserNameAlreadyUsed(String userNameToCheck) {
        Optional<DBUser> optionalUser = Optional.ofNullable(userRepository.findByUsername(userNameToCheck));
        return optionalUser.isPresent();
    }
}
