package com.nnk.springboot;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;
    private static User firstUser;
    private static User secondUser;

    private static List<User> users;

    @BeforeEach
    public void setUp() {
        Integer id = 1;
        String userName = "userName";
        String password = "password";
        String fullName = "fullName";
        String role = "role";

        Integer id2 = 2;
        String userName2 = "userName2";
        String password2 = "password2";
        String fullName2 = "fullName2";
        String role2 = "role2";

        firstUser = new User(id, userName, password, fullName, role);
        secondUser = new User(id2, userName2, password2, fullName2, role2);

        users = List.of(firstUser, secondUser);
    }

    @Test
    public void testAddUser() {
        User testUser = new User(1, "userName", "password", "fullName", "role");
        User addedUserWithEncryptedPassword = new User(1, "userName", "encodedPassword", "fullName", "role");

        System.out.println("userService: " + userService);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        System.out.println("encoder: " + passwordEncoder);


        when(userRepository.save(any(User.class))).thenReturn(addedUserWithEncryptedPassword);

        User resultingUser = userService.addUser(testUser);

        assertEquals(testUser.getRole(), resultingUser.getRole());
        assertEquals(testUser.getUsername(), resultingUser.getUsername());
        assertEquals(testUser.getFullName(), resultingUser.getFullName());
        assertEquals(testUser.getId(), resultingUser.getId());

        verify(passwordEncoder, times(1)).encode("password");
    }


    @Test
    public void testShowUserUpdateFormForUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(firstUser));

        User foundUser = userService.showUpdateFormForUser(firstUser.getId());

        assertEquals(foundUser, firstUser);
        assertEquals("", foundUser.getPassword());
    }

    @Test
    public void testShowUserUpdateFormUserNotFound() {

        assertThrows(IllegalArgumentException.class, () -> userService.showUpdateFormForUser(firstUser.getId()));

    }

    @Test
    public void testGetUserById() {

        when(userRepository.findById(firstUser.getId())).thenReturn(Optional.ofNullable(firstUser));
        User foundUser = userService.getUserById(firstUser.getId());
        assertEquals(firstUser, foundUser);

    }

    @Test
    public void testUpdateFoundUser() {

        when(userRepository.findById(firstUser.getId())).thenReturn(Optional.ofNullable(firstUser));
        User updatedUser = userService.updateUser(firstUser.getId(), secondUser);
        assertEquals(firstUser.getId(), updatedUser.getId());
        assertEquals(secondUser.getRole(), updatedUser.getRole());
        assertEquals(secondUser.getFullName(), updatedUser.getFullName());
        assertEquals(secondUser.getUsername(), updatedUser.getUsername());

        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void testUpdateUserNotFound() {

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(firstUser.getId(), secondUser));
        verify(userRepository, times(0)).save(any(User.class));

    }

    @Test
    public void testDeleteUserFound() {
        when(userRepository.findById(firstUser.getId())).thenReturn(Optional.ofNullable(firstUser));
        userService.deleteUserById(firstUser.getId());
        verify(userRepository, times(1)).delete(firstUser);
    }

    @Test
    public void testDeleteUserNotFound() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(firstUser.getId()));
        verify(userRepository, times(0)).delete(firstUser);
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(users);
        List<User> foundUsers = userService.getAllUsers();
        assertEquals(users, foundUsers);
    }

}
