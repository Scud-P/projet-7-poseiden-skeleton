package com.nnk.springboot;

import com.nnk.springboot.controllers.ErrorController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ErrorControllerTest {

    @Autowired
    private ErrorController errorController;

    @Test
    public void accessDeniedTest() {
        String accessDenied = errorController.accessDenied();
        assertEquals("/403", accessDenied);
    }
}
