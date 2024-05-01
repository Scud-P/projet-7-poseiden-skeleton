package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/DBUser/list")
    public String home(Model model) {
        List<DBUser> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "DBUser/list";
    }

    @GetMapping("/DBUser/add")
    public String addUser(DBUser bid) {
        return "DBUser/add";
    }

    @PostMapping("/DBUser/validate")
    public String validate(@Valid DBUser DBUser, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            userService.addUser(DBUser);
            model.addAttribute("users", userService.getAllUsers());
            return "redirect:/DBUser/list";
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
    public String updateUser(@PathVariable("id") Integer id, @Valid DBUser DBUser,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "DBUser/update";
        }
        userService.updateUser(id, DBUser);
        model.addAttribute("users", userService.getAllUsers());
        return "redirect:/DBUser/list";
    }

    @GetMapping("/DBUser/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.deleteUserById(id);
        model.addAttribute("users", userService.getAllUsers());
        return "redirect:/DBUser/list";
    }
}
