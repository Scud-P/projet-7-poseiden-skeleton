package com.nnk.springboot;

import com.nnk.springboot.controllers.WebController;
import com.nnk.springboot.services.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class WebControllerTest {

    @Autowired
    private WebController webController;

    @MockBean
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomeWhenAuthenticationIsValid() {

        UserDetails userDetails = User
                .withUsername("testUser")
                .password("V4l1dPassword")
                .roles("USER")
                .build();

        when(securityService.isAuthenticated()).thenReturn(true);
        when(securityService.getCurrentUserDetails()).thenReturn(userDetails);

        String home = webController.home();
        assertEquals("home", home);
    }

    @Test
    public void testHomeWhenNotAuthenticated() {
        when(securityService.isAuthenticated()).thenReturn(false);

        String login = webController.home();
        assertEquals("login", login);
    }

    @Test
    public void testHomeWhenUserDetailsNull() {
        when(securityService.isAuthenticated()).thenReturn(true);
        when(securityService.getCurrentUserDetails()).thenReturn(null);

        String login = webController.home();
        assertEquals("login", login);
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testLoginWithError() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Invalid username or password."));
    }
}
