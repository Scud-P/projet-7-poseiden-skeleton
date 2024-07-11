package com.nnk.springboot;

import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.domain.DTO.DBUserDTO;
import com.nnk.springboot.domain.parameter.DBUserParameter;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private static List<DBUser> dbUsers;

    private static DBUserDTO firstDBUserDTO;
    private static DBUserDTO secondDBUserDTO;
    private static List<DBUserDTO> dbUserDTOs;

    private static DBUserParameter firstDBUserParam;
    private static DBUserParameter secondDBUserParam;
    private static List<DBUserParameter> dbUserParams;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {

        Integer id = 1;
        String userName = "userName";
        String password = "P4ssW0rd!";
        String fullName = "fullName";
        String role = "role";

        Integer id2 = 2;
        String userName2 = "userName2";
        String password2 = "P4ssW0rd!2";
        String fullName2 = "fullNameTwo";
        String role2 = "role2";

        firstDBUser = new DBUser(id, userName, password, fullName, role);
        secondDBUser = new DBUser(id2, userName2, password2, fullName2, role2);
        dbUsers = List.of(firstDBUser, secondDBUser);

        firstDBUserDTO = new DBUserDTO(id, userName, password, fullName, role);
        secondDBUserDTO = new DBUserDTO(id2, userName2, password2, fullName2, role2);
        dbUserDTOs = List.of(firstDBUserDTO, secondDBUserDTO);

        firstDBUserParam = new DBUserParameter(id, userName, password, fullName, role);
        secondDBUserParam = new DBUserParameter(id2, userName2, password2, fullName2, role2);
        dbUserParams = List.of(firstDBUserParam, secondDBUserParam);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetHome() {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        when(userService.getAllUsers()).thenReturn(dbUserDTOs);

        String home = userController.home(model);

        assertEquals("DBUser/list", home);
        verify(model).addAttribute("dbUserDTOs", dbUserDTOs);
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

        verify(userService, times(1)).addUser(any(DBUserParameter.class));
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

        verify(userService, times(0)).addUser(any(DBUserParameter.class));
    }

    @Test
    public void testValidateUserInvalidPasswordRegex() throws Exception {

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/DBUser/validate")
                        .param("username", firstDBUser.getUsername())
                        .param("password", "password")
                        .param("fullName", firstDBUser.getFullName())
                        .param("role", firstDBUser.getRole()))
                .andExpect(status().isOk())
                .andExpect(view().name("DBUser/add"));

        verify(userService, times(0)).addUser(any(DBUserParameter.class));
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

        mockMvc.perform(post("/DBUser/update")
                        .param("id", String.valueOf(firstDBUser.getId()))
                        .param("username", secondDBUser.getUsername())
                        .param("password", secondDBUser.getPassword())
                        .param("fullName", secondDBUser.getFullName())
                        .param("role", secondDBUser.getRole()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/DBUser/list"));

        secondDBUserParam.setId(firstDBUserParam.getId());

        verify(userService, times(1)).updateUser(1, secondDBUserParam);
    }

    @Test
    public void testUpdateUserInvalidInput() throws Exception {
        mockMvc.perform(post("/DBUser/update")
                        .param("id", "1")
                        .param("username", "invalid_username")  // Invalid input
                        .param("password", "validPassword123!")
                        .param("fullName", "John Doe")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("DBUser/update"));

        verify(userService, never()).updateUser(anyInt(), any(DBUserParameter.class));
    }

    @Test
    public void testShowUpdateForm() throws Exception {

        when(userService.showUpdateFormForUser(anyInt())).thenReturn(firstDBUserParam);

        mockMvc.perform(get("/DBUser/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("DBUser/update"));
    }

    @Test
    public void testHomeNotAdmin() throws Exception {

        mockMvc.perform(get("/DBUser/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }
}
