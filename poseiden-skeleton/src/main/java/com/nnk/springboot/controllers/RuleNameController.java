package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.RuleNameDTO;
import com.nnk.springboot.domain.parameter.RuleNameParameter;
import com.nnk.springboot.services.RuleNameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        List<RuleNameDTO> ruleNameDTOs = ruleNameService.getAllRuleNames();
        model.addAttribute("ruleNameDTOs", ruleNameDTOs);
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        model.addAttribute("ruleNameParameter", new RuleNameParameter());
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleNameParameter ruleNameParameter, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            ruleNameService.addRuleName(ruleNameParameter);
            model.addAttribute("ruleNameDTOs", ruleNameService.getAllRuleNames());
            return "redirect:/ruleName/list";
        }
        model.addAttribute("ruleNameParameter", ruleNameParameter);
        return "ruleName/add";
    }

    // TODO Can I just get the Parameter directly?

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleNameDTO ruleNameDTO = ruleNameService.getRuleNameDTOById(id);
        RuleNameParameter ruleNameParameter = ruleNameService.mapRuleNameDTOToParameter(ruleNameDTO);
        model.addAttribute("ruleNameParameter", ruleNameParameter);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleNameParameter ruleNameParameter,
                                 BindingResult result, Model model) {

        if (!result.hasErrors()) {
            ruleNameService.updateRuleName(id, ruleNameParameter);
            model.addAttribute("ruleNames", ruleNameService.getAllRuleNames());
            return "redirect:/ruleName/list";
        }
        return "ruleName/update";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        ruleNameService.deleteRuleName(id);
        model.addAttribute("ruleNames", ruleNameService.getAllRuleNames());
        return "redirect:/ruleName/list";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
