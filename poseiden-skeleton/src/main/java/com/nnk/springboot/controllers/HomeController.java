package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to handle the home page requests.
 */
@Controller
public class HomeController {
    /**
     * Handles requests to the root URL ("/").
     *
     * @param model the model to add attributes to.
     * @return the name of the view to display for the home page.
     */
    @RequestMapping("/")
    public String home(Model model) {
        return "home";
    }
}
