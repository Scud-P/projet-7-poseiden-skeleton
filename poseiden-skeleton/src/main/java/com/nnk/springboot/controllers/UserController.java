package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.DBUserDTO;
import com.nnk.springboot.domain.parameter.DBUserParameter;
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
        List<DBUserDTO> dbUserDTOs = userService.getAllUsers();
        model.addAttribute("dbUserDTOs", dbUserDTOs);
        return "DBUser/list";
    }

    @PostMapping("/DBUser/validate")
    public String validate(@Valid DBUserParameter dbUserParameter, BindingResult result, Model model) {

        // Check if password meets the criteria (8-20 characters, at least one lowercase and one uppercase letter,
        // at least one number and at least one special character)
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

        if (!dbUserParameter.getPassword().matches(passwordPattern)) {
            result.rejectValue("password", "error.dbUser",
                    "Password must be 8-20 characters long, contain at least one digit," +
                            " one lowercase letter, one uppercase letter, one special character (@#$%^&+=!)" +
                            ", and have no spaces.");
        }
        if (!result.hasErrors()) {
            try {
                userService.addUser(dbUserParameter);
                model.addAttribute("dbUserDTOs", userService.getAllUsers());
                return "redirect:/DBUser/list";

            } catch (IllegalArgumentException e) {
                result.rejectValue("username", "error.dbUser", "Username already exists");
                model.addAttribute("dbUserParameter", dbUserParameter);
                System.out.println("Validation errors: " + result.getAllErrors());
                System.out.println("Username already used");
                return "DBUser/add";
            }
        }
        model.addAttribute("dbUserParameter", dbUserParameter);
        return "DBUser/add";
    }

    @GetMapping("/DBUser/add")
    public String addUser(DBUserParameter dbUserParameter) {
        return "DBUser/add";
    }

    @GetMapping("/DBUser/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        DBUserParameter dbUserParameter = userService.showUpdateFormForUser(id);
        model.addAttribute("DBUserParameter", dbUserParameter);
        return "DBUser/update";
    }

    @PostMapping("/DBUser/update")
    public String updateUser(@Valid DBUserParameter dbUserParameter, BindingResult result, Model model) {

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

        if (!dbUserParameter.getPassword().matches(passwordPattern)) {
            result.rejectValue("password", "error.dbUser",
                    "Password must be 8-20 characters long, contain at least one digit," +
                            " one lowercase letter, one uppercase letter, one special character (@#$%^&+=!)" +
                            ", and have no spaces.");
        }

        if (!result.hasErrors()) {
            try {
                userService.updateUser(dbUserParameter.getId(), dbUserParameter);
                model.addAttribute("dbUserDTOs", userService.getAllUsers());
                return "redirect:/DBUser/list";

            } catch (IllegalArgumentException e) {
                result.rejectValue("username", "error.dbUser", "Username already exists");
                model.addAttribute("dbUserParameter", dbUserParameter);
            }
        }
        model.addAttribute("dbUserParameter", dbUserParameter);
        return "DBUser/update";
    }

    @GetMapping("/DBUser/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.deleteUserById(id);
        model.addAttribute("users", userService.getAllUsers());
        return "redirect:/DBUser/list";
    }
}
