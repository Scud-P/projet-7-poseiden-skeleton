package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.RuleNameDTO;
import com.nnk.springboot.domain.parameter.RuleNameParameter;
import com.nnk.springboot.services.RuleNameService;
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

/**
 * Controller for managing Rule Names.
 */
@Controller
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;

    /**
     * Displays the list rule names.
     *
     * @param model the model to which the list of rule names is added
     * @return the view name for displaying the rule names
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        List<RuleNameDTO> ruleNameDTOs = ruleNameService.getAllRuleNames();
        model.addAttribute("ruleNameDTOs", ruleNameDTOs);
        return "ruleName/list";
    }

    /**
     * Displays the form for adding a new rule name
     *
     * @param model the model to which a new RuleNameParameter object is added
     * @return the view name for adding a new rule name
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        model.addAttribute("ruleNameParameter", new RuleNameParameter());
        return "ruleName/add";
    }

    /**
     * Validates and saves a new rule name
     *
     * @param ruleNameParameter the parameter object containing the rule name data
     * @param result            the binding result for validation errors
     * @param model             the model to which the updated list of rule names is added
     * @return the view name to redirect to after saving the rule name, or the view name for adding a rule name if there are validation errors
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleNameParameter ruleNameParameter, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                ruleNameService.addRuleName(ruleNameParameter);
                model.addAttribute("ruleNameDTOs", ruleNameService.getAllRuleNames());
                return "redirect:/ruleName/list";
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        model.addAttribute("ruleNameParameter", ruleNameParameter);
        return "ruleName/add";
    }

    /**
     * Displays the form for updating an existing rule name.
     *
     * @param id    the ID of the rule name to update
     * @param model the model to which the updated list of rule names or error messages are added
     * @return the view name for updating a rule name, or the view name for displaying an error message if an exception occurs
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        try {
            RuleNameParameter ruleNameParameter = ruleNameService.getRuleNameParameterById(id);
            model.addAttribute("ruleNameParameter", ruleNameParameter);
            return "ruleName/update";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Updates an existing rule name.
     *
     * @param id                the ID of the rating to update
     * @param ruleNameParameter the object created after the user fills the form and containing the updated rule name data
     * @param result            the binding result for validation
     * @param model             the model to which the updated list of rule names or error messages are added
     * @return the redirect URL to the rule name list if the update is successful, the update form view if validation fails,
     * or the error view if an exception occurs
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleNameParameter ruleNameParameter,
                                 BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                ruleNameService.updateRuleName(id, ruleNameParameter);
                model.addAttribute("ruleNames", ruleNameService.getAllRuleNames());
                return "redirect:/ruleName/list";

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        return "ruleName/update";
    }

    /**
     * Deletes the targeted rule name.
     *
     * @param id    the ID of the rule name to delete
     * @param model the model to which the updated list of rule names or error messages are added
     * @return the redirect URL to the rule name list if the deletion is successful, or the error view if an exception occurs
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        try {
            ruleNameService.deleteRuleName(id);
            model.addAttribute("ruleNames", ruleNameService.getAllRuleNames());
            return "redirect:/ruleName/list";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}
