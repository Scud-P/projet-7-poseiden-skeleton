package com.nnk.springboot;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.domain.DTO.DBUserDTO;
import com.nnk.springboot.domain.parameter.DBUserParameter;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import com.nnk.springboot.util.DBUserMapper;
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
public class DBUserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private DBUserMapper dbUserMapper;

    @MockBean
    private UserRepository userRepository;
    private static DBUser firstDBUser;
    private static DBUser secondDBUser;

    private static List<DBUser> DBUsers;

    private static DBUserParameter dbUserParameter;

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

        firstDBUser = new DBUser(id, userName, password, fullName, role);
        secondDBUser = new DBUser(id2, userName2, password2, fullName2, role2);

        DBUsers = List.of(firstDBUser, secondDBUser);

        dbUserParameter = new DBUserParameter
                (firstDBUser.getId(), firstDBUser.getUsername(), firstDBUser.getPassword(), firstDBUser.getFullName(), firstDBUser.getRole());
    }

    @Test
    public void testAddUser() {

        DBUser testDBUser = new DBUser(1, "userName", "password", "fullName", "role");
        DBUser addedDBUserWithEncryptedPassword = new DBUser(1, "userName", "encodedPassword", "fullName", "role");

        System.out.println("userService: " + userService);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        System.out.println("encoder: " + passwordEncoder);


        when(userRepository.save(any(DBUser.class))).thenReturn(addedDBUserWithEncryptedPassword);

        DBUser resultingDBUser = userService.addUser(dbUserMapper.toDBUserParameter(testDBUser));

        assertEquals(testDBUser.getRole(), resultingDBUser.getRole());
        assertEquals(testDBUser.getUsername(), resultingDBUser.getUsername());
        assertEquals(testDBUser.getFullName(), resultingDBUser.getFullName());
        assertEquals(testDBUser.getId(), resultingDBUser.getId());

        verify(passwordEncoder, times(1)).encode("password");
    }


    @Test
    public void testShowUserUpdateFormForUser() {

        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(firstDBUser));

        DBUserParameter expectedParameter = dbUserMapper.toDBUserParameter(firstDBUser);
        expectedParameter.setPassword("");

        DBUserParameter foundDBUserParameter = userService.showUpdateFormForUser(firstDBUser.getId());

        assertEquals(expectedParameter, foundDBUserParameter);
        assertEquals("", foundDBUserParameter.getPassword());
    }

    @Test
    public void testShowUserUpdateFormUserNotFound() {

        assertThrows(IllegalArgumentException.class, () -> userService.showUpdateFormForUser(firstDBUser.getId()));

    }

    @Test
    public void testGetUserById() {

        when(userRepository.findById(firstDBUser.getId())).thenReturn(Optional.ofNullable(firstDBUser));
        DBUser foundDBUser = userService.getUserById(firstDBUser.getId());
        assertEquals(firstDBUser, foundDBUser);

    }

    @Test
    public void testUpdateFoundUser() {

        userService.addUser(dbUserParameter);

        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(firstDBUser));

        DBUserParameter updatedDBUserParameter = new DBUserParameter(
                firstDBUser.getId(),secondDBUser.getUsername(),secondDBUser.getPassword(),secondDBUser.getFullName(),secondDBUser.getRole()
        );

        DBUser updatedDBUser = userService.updateUser(firstDBUser.getId(), updatedDBUserParameter);

        assertEquals(firstDBUser.getId(), updatedDBUser.getId());
        assertEquals(secondDBUser.getRole(), updatedDBUser.getRole());
        assertEquals(secondDBUser.getFullName(), updatedDBUser.getFullName());
        assertEquals(secondDBUser.getUsername(), updatedDBUser.getUsername());

        verify(userRepository, times(1)).save(updatedDBUser);
    }

    @Test
    public void testUpdateUserNotFound() {

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(firstDBUser.getId(), dbUserParameter));
        verify(userRepository, times(0)).save(any(DBUser.class));

    }

    @Test
    public void testDeleteUserFound() {
        when(userRepository.findById(firstDBUser.getId())).thenReturn(Optional.ofNullable(firstDBUser));
        userService.deleteUserById(firstDBUser.getId());
        verify(userRepository, times(1)).delete(firstDBUser);
    }

    @Test
    public void testDeleteUserNotFound() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(firstDBUser.getId()));
        verify(userRepository, times(0)).delete(firstDBUser);
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(DBUsers);
        List<DBUserDTO> foundDBUsers = userService.getAllUsers();
        assertEquals(2, foundDBUsers.size());
    }

    @Test
    public void testGetByUserName() {
        when(userRepository.findByUsername(firstDBUser.getUsername())).thenReturn(firstDBUser);
        DBUser foundUser = userService.getByUsername(firstDBUser.getUsername());
        assertEquals(firstDBUser, foundUser);
    }

}
