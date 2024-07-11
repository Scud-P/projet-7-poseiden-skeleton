package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.CurvePointDTO;
import com.nnk.springboot.domain.parameter.CurvePointParameter;
import com.nnk.springboot.services.CurvePointService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CurveController {

    @Autowired
    private CurvePointService curvePointService;


    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        List<CurvePointDTO> curvePointDTOs = curvePointService.getAllCurvePoints();
        model.addAttribute("curvePointDTOs", curvePointDTOs);
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(Model model) {
        model.addAttribute("curvePointParameter", new CurvePointParameter());
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePointParameter curvePointParameter, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            curvePointService.addCurvePoint(curvePointParameter);
            model.addAttribute("curvePointDTOs", curvePointService.getAllCurvePoints());
            return "redirect:/curvePoint/list";
        }
        model.addAttribute("curvePointParameter", curvePointParameter);
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePointParameter curvePointParameter = curvePointService.getCurvePointParameterById(id);
        model.addAttribute("curvePointParameter", curvePointParameter);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePointParameter curvePointParameter,
                                   BindingResult result, Model model) {

        if (!result.hasErrors()) {
            curvePointService.updateCurvePoint(id, curvePointParameter);
            model.addAttribute("curvePoints", curvePointService.getAllCurvePoints());
            return "redirect:/curvePoint/list";
        }
        return "curvePoint/update";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        curvePointService.deleteCurvePoint(id);
        model.addAttribute("curvePoints", curvePointService.getAllCurvePoints());
        return "redirect:/curvePoint/list";
    }
}