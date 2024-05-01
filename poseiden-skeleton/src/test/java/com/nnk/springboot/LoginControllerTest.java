package com.nnk.springboot;

import com.nnk.springboot.controllers.LoginController;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginController loginController;

    @MockBean
    private UserRepository userRepository;


    @Test
    @WithMockUser
    public void testError() throws Exception {
        String expectedErrorMessage = "You are not authorized for the requested data.";
        mockMvc.perform(get("/app/error"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMsg"))
                .andExpect(view().name("403"))
                .andExpect(model().attribute("errorMsg", expectedErrorMessage));
    }

    @Test
    @WithMockUser
    public void testGetAllUserArticles() throws Exception {
        mockMvc.perform(get("/app/secure/article-details"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("DBUser/list"));
    }
}
