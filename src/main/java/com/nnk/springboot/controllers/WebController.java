package com.nnk.springboot.controllers;

import com.nnk.springboot.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller to handle the login and home page requests.
 */
@Controller
public class WebController {

    @Autowired
    private SecurityService securityService;

    /**
     * Handles requests to the root URL ("/").
     *
     * @return the name of the view to display for the home page.
     */
    @RequestMapping("/")
    public String home() {
        if (!securityService.isAuthenticated()) {
            return "login";
        }
        UserDetails userDetails = securityService.getCurrentUserDetails();
        if (userDetails == null) {
            return "login";
        }
        return "home";
    }

    /**
     * Handles requests to the login URL ("/login").
     * This method processes requests to the login page. If the request contains an `error` parameter,
     * it adds an appropriate error message to the model. This message will be displayed on the login page.
     * The `error` parameter is optional. If provided and equals "true", a generic error message indicating
     * invalid username or password is set.
     *
     * @param error an optional query parameter indicating if there was an error during login
     * @param model the model to which attributes are added for the view
     * @return the name of the view to display for the login page
     */
    @RequestMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }
        return "login";
    }
}
