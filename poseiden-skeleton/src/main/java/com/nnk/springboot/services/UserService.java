package com.nnk.springboot.services;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.domain.DTO.DBUserDTO;
import com.nnk.springboot.domain.parameter.DBUserParameter;
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
        if (isUserNameAlreadyUsed(dbUserParameter.getUsername())) {
            throw new IllegalArgumentException("userName already used");
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


        if (!existingDBUser.getUsername().equals(dbUserParameter.getUsername()) && isUserNameAlreadyUsed(dbUserParameter.getUsername())) {
            System.out.println("Service caught that the username already exists");
            throw new IllegalArgumentException("Username already exists");
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

    public boolean isUserNameAlreadyUsed(String userNameToCheck) {
        Optional<DBUser> optionalUser = Optional.ofNullable(userRepository.findByUsername(userNameToCheck));
        return optionalUser.isPresent();
    }

    public DBUserParameter getDBUserParameterById(int id) {
        DBUser dbUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No User found for id " + id));
        return dbUserMapper.toDBUserParameter(dbUser);
    }
}
