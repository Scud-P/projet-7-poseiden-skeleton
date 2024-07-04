package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/DBUser/list")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/403";
        }
        List<DBUser> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "DBUser/list";
    }

    @GetMapping("/DBUser/add")
    public String addUser(DBUser user) {
        return "DBUser/add";
    }

    @PostMapping("/DBUser/validate")
    public String validate(@Valid DBUser dbUser, BindingResult result, Model model) {

        // Check if password meets the criteria (8-20 characters, at least one lowercase and one uppercase letter,
        // at least one number and at least one special character)
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

        if (!dbUser.getPassword().matches(passwordPattern)) {
            result.rejectValue("password", "error.dbUser",
                    "Password must be 8-20 characters long, contain at least one digit," +
                            " one lowercase letter, one uppercase letter, one special character (@#$%^&+=!)" +
                            ", and have no spaces.");
        }
        if (!result.hasErrors()) {
            try {
                userService.addUser(dbUser);
                model.addAttribute("users", userService.getAllUsers());
                return "redirect:/DBUser/list";
            } catch (IllegalArgumentException e) {
                result.rejectValue("username", "error.dbUser", "Username already exists");
                return "DBUser/add";
            }
        }
        return "DBUser/add";
    }

    @GetMapping("/DBUser/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        DBUser DBUser = userService.showUpdateFormForUser(id);
        model.addAttribute("DBUser", DBUser);
        return "DBUser/update";
    }

    @PostMapping("/DBUser/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid DBUser dbUser,
                             BindingResult result, Model model) {

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

        if (!dbUser.getPassword().matches(passwordPattern)) {
            result.rejectValue("password", "error.dbUser",
                    "Password must be 8-20 characters long, contain at least one digit," +
                            " one lowercase letter, one uppercase letter, one special character (@#$%^&+=!)" +
                            ", and have no spaces.");
        }

        if (!result.hasErrors()) {
            try {
                userService.updateUser(id, dbUser);
                model.addAttribute("users", userService.getAllUsers());
                return "redirect:/DBUser/list";
            } catch (IllegalArgumentException e) {
                result.rejectValue("username", "error.dbUser", "Username already exists");
                return "DBUser/update";
            }
        }
        return "DBUser/update";
    }

    @GetMapping("/DBUser/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.deleteUserById(id);
        model.addAttribute("users", userService.getAllUsers());
        return "redirect:/DBUser/list";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
