package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.DBUserDTO;
import com.nnk.springboot.domain.parameter.DBUserParameter;
import com.nnk.springboot.exceptions.UserNameAlreadyUsedException;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller class for managing DBUser entities.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Displays the list users.
     *
     * @param model the model to which the list of users is added
     * @return the view name for displaying the users or a redirection to the access denied (403) page
     */
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

    /**
     * Validates and saves a new user
     *
     * @param dbUserParameter the parameter object containing the user data
     * @param result         the binding result for validation errors
     * @param model          the model to which the updated list of users is added
     * @return the view name to redirect to after saving the user, or the view name for adding a user if there are validation errors
     * or the username is already used, or finally the view name for the error page in case an IllegalArgumentException is caught
     */
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

                // Catches the UserNameAlreadyUsedException, we can't afford to have two users with the same userName since
                // we load grantedAuthorities based on the userName
            } catch (UserNameAlreadyUsedException usedException) {
                result.rejectValue("username", "error.dbUser", "Username already exists");
                model.addAttribute("dbUserParameter", dbUserParameter);

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        model.addAttribute("dbUserParameter", dbUserParameter);
        return "DBUser/add";
    }

    /**
     * Displays the form for adding a new user
     *
     * @return the view name for adding a new user
     */
    @GetMapping("/DBUser/add")
    public String addUser(DBUserParameter dbUserParameter) {
        return "DBUser/add";
    }

    /**
     * Displays the form for updating an existing user.
     *
     * @param id    the ID of the user to update
     * @param model the model to which the updated list of users or error messages are added
     * @return the view name for updating a user, or the view name for displaying an error message if an exception occurs
     */
    @GetMapping("/DBUser/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            DBUserParameter dbUserParameter = userService.showUpdateFormForUser(id);
            model.addAttribute("DBUserParameter", dbUserParameter);
            return "DBUser/update";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Validates and saves a new user
     *
     * @param dbUserParameter the parameter object containing the user data
     * @param result         the binding result for validation errors
     * @param model          the model to which the updated list of users is added
     * @return the view name to redirect to after saving the user, or the view name for adding a user if there are validation errors
     * or the username is already used, or finally the view name for the error page in case an IllegalArgumentException is caught
     */
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

                // Catches the UserNameAlreadyUsedException, we can't afford to have two users with the same userName since
                // we load grantedAuthorities based on the userName
            } catch (UserNameAlreadyUsedException usedException) {
                result.rejectValue("username", "error.dbUser", "Username already exists");
                model.addAttribute("dbUserParameter", dbUserParameter);

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        model.addAttribute("dbUserParameter", dbUserParameter);
        return "DBUser/update";
    }

    /**
     * Deletes the targeted user.
     *
     * @param id    the ID of the user to delete
     * @param model the model to which the updated list of users or error messages are added
     * @return the redirect URL to the user list if the deletion is successful, or the error view if an exception occurs
     */
    @GetMapping("/DBUser/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        try {
            userService.deleteUserById(id);
            model.addAttribute("users", userService.getAllUsers());
            return "redirect:/DBUser/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}
