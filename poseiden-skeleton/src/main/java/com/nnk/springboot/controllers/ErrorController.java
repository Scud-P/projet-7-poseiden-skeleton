package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle access denial.
 */
@Controller
public class ErrorController {

    /**
     * Handles requests to the /403 endpoint.
     *
     * @return The name of the view to display for access denied errors.
     */
    @GetMapping("/403")
    public String accessDenied() {
        return "/403";
    }
}
