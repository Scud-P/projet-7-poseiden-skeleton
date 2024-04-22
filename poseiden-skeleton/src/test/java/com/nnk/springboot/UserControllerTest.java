package com.nnk.springboot;

import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Model model;

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
    public void testGetHome() {

        when(userService.getAllUsers()).thenReturn(users);
        String home = userController.home(model);

        assertEquals("user/list", home);
        verify(model).addAttribute("users", users);
    }

    @Test
    public void testAddUser() throws Exception {

        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    public void testAddUserValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/user/validate")
                        .param("username", firstUser.getUsername())
                        .param("password", firstUser.getPassword())
                        .param("fullName", firstUser.getFullName())
                        .param("role", firstUser.getRole()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    public void testValidateUserInvalidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/user/validate")
                        .param("username", "")
                        .param("password", firstUser.getPassword())
                        .param("fullName", firstUser.getFullName())
                        .param("role", firstUser.getRole()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

        verify(userService, times(0)).addUser(any(User.class));

    }

    @Test
    public void testDeleteUser() throws Exception {

        when(userService.getUserById(anyInt())).thenReturn(firstUser);

        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).deleteUserById(firstUser.getId());
    }

    @Test
    public void testUpdateUserValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        when(userService.updateUser(anyInt(), any(User.class))).thenReturn(firstUser);

        mockMvc.perform(post("/user/update/1")
                .param("username", secondUser.getUsername())
                .param("password", secondUser.getPassword())
                .param("fullName", secondUser.getFullName())
                .param("role", secondUser.getRole()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        User updatedUser = new User(firstUser.getId(), secondUser.getUsername(), secondUser.getPassword(), secondUser.getFullName(), secondUser.getRole());

        verify(userService, times(1)).updateUser(firstUser.getId(), updatedUser);
    }

    @Test
    public void testUpdateUserInvalidInput() throws Exception {

        mockMvc.perform(post("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));

        verify(userService, times(0)).updateUser(anyInt(), any(User.class));

    }

    @Test
    public void testShowUpdateForm() throws Exception {

        when(userService.showUpdateFormForUser(firstUser.getId())).thenReturn(firstUser);

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));
    }


}
