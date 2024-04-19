package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
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

@Controller
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        List<RuleName> ruleNames = ruleNameService.getAllRuleNames();
        model.addAttribute("ruleNames", ruleNames);
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleNameToAdd, BindingResult result, Model model) {

        if(!result.hasErrors()) {
            ruleNameService.addRuleName(ruleNameToAdd);
            model.addAttribute("ruleNames", ruleNameService.getAllRuleNames());
            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName ruleNameToUpdate = ruleNameService.getRuleNameById(id);
        model.addAttribute("ruleName", ruleNameToUpdate);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleNameToUpdate,
                             BindingResult result, Model model) {

        if(!result.hasErrors()) {
            ruleNameService.updateRuleName(id, ruleNameToUpdate);
            model.addAttribute("ruleNames", ruleNameService.getAllRuleNames());
            return "redirect:/ruleName/list";
        }
        return "ruleName/update";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        RuleName ruleNameToDelete = ruleNameService.getRuleNameById(id);
        ruleNameService.deleteRuleName(ruleNameToDelete);
        model.addAttribute("ruleNames", ruleNameService.getAllRuleNames());
        return "redirect:/ruleName/list";
    }
}
