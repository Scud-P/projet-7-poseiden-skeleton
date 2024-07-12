package com.nnk.springboot;

import com.nnk.springboot.controllers.HomeController;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;


@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private HomeController homeController;

    @Mock
    private Model model;

    @Test
    @WithMockUser
    public void testHome() {
        String home = homeController.home(model);
        assertEquals("home", home);
    }
}
