package com.nnk.springboot;

import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.DBUser;
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
public class DBUserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Model model;

    private static DBUser firstDBUser;

    private static DBUser secondDBUser;

    private static List<DBUser> DBUsers;

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
    }

    @Test
    public void testGetHome() {

        when(userService.getAllUsers()).thenReturn(DBUsers);
        String home = userController.home(model);

        assertEquals("DBUser/list", home);
        verify(model).addAttribute("users", DBUsers);
    }

    @Test
    public void testAddUser() throws Exception {

        mockMvc.perform(get("/DBUser/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("DBUser/add"));
    }

    @Test
    public void testAddUserValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/DBUser/validate")
                        .param("username", firstDBUser.getUsername())
                        .param("password", firstDBUser.getPassword())
                        .param("fullName", firstDBUser.getFullName())
                        .param("role", firstDBUser.getRole()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/DBUser/list"));

        verify(userService, times(1)).addUser(any(DBUser.class));
    }

    @Test
    public void testValidateUserInvalidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/DBUser/validate")
                        .param("username", "")
                        .param("password", firstDBUser.getPassword())
                        .param("fullName", firstDBUser.getFullName())
                        .param("role", firstDBUser.getRole()))
                .andExpect(status().isOk())
                .andExpect(view().name("DBUser/add"));

        verify(userService, times(0)).addUser(any(DBUser.class));

    }

    @Test
    public void testDeleteUser() throws Exception {

        when(userService.getUserById(anyInt())).thenReturn(firstDBUser);

        mockMvc.perform(get("/DBUser/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/DBUser/list"));

        verify(userService, times(1)).deleteUserById(firstDBUser.getId());
    }

    @Test
    public void testUpdateUserValidInput() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        when(userService.updateUser(anyInt(), any(DBUser.class))).thenReturn(firstDBUser);

        mockMvc.perform(post("/DBUser/update/1")
                        .param("username", secondDBUser.getUsername())
                        .param("password", secondDBUser.getPassword())
                        .param("fullName", secondDBUser.getFullName())
                        .param("role", secondDBUser.getRole()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/DBUser/list"));

        DBUser updatedDBUser = new DBUser(firstDBUser.getId(), secondDBUser.getUsername(), secondDBUser.getPassword(), secondDBUser.getFullName(), secondDBUser.getRole());

        verify(userService, times(1)).updateUser(firstDBUser.getId(), updatedDBUser);
    }

    @Test
    public void testUpdateUserInvalidInput() throws Exception {

        mockMvc.perform(post("/DBUser/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("DBUser/update"));

        verify(userService, times(0)).updateUser(anyInt(), any(DBUser.class));

    }

    @Test
    public void testShowUpdateForm() throws Exception {

        when(userService.showUpdateFormForUser(firstDBUser.getId())).thenReturn(firstDBUser);

        mockMvc.perform(get("/DBUser/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("DBUser/update"));
    }
}
