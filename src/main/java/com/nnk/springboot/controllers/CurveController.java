package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.CurvePointDTO;
import com.nnk.springboot.domain.parameter.CurvePointParameter;
import com.nnk.springboot.services.CurvePointService;
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
 * Controller for managing Curve Points.
 */
@Controller
public class CurveController {

    @Autowired
    private CurvePointService curvePointService;

    /**
     * Displays the list curve points.
     *
     * @param model the model to which the list curve point is added
     * @return the view name for displaying the curve points
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        List<CurvePointDTO> curvePointDTOs = curvePointService.getAllCurvePoints();
        model.addAttribute("curvePointDTOs", curvePointDTOs);
        return "curvePoint/list";
    }

    /**
     * Displays the form for adding a new curve point
     *
     * @param model the model to which a new CurvePointParameter object is added
     * @return the view name for adding a new curve point
     */
    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(Model model) {
        model.addAttribute("curvePointParameter", new CurvePointParameter());
        return "curvePoint/add";
    }

    /**
     * Validates and saves a new curve point
     *
     * @param curvePointParameter the parameter object containing the curve point data
     * @param result              the binding result for validation errors
     * @param model               the model to which the updated list of curve points is added
     * @return the view name to redirect to after saving the curve point,  or the view name for displaying
     * an error message if an exception occurs,or the view name for adding a curve point if there are validation errors
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePointParameter curvePointParameter, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                curvePointService.addCurvePoint(curvePointParameter);
                model.addAttribute("curvePointDTOs", curvePointService.getAllCurvePoints());
                return "redirect:/curvePoint/list";

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        model.addAttribute("curvePointParameter", curvePointParameter);
        return "curvePoint/add";
    }

    /**
     * Displays the form for updating an existing curve point.
     *
     * @param id    the ID of the curve point to update
     * @param model the model to which the updated list of curve points or error messages are added
     * @return the view name for updating a curve point, or the view name for displaying an error message if an exception occurs
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        try {
            CurvePointParameter curvePointParameter = curvePointService.getCurvePointParameterById(id);
            model.addAttribute("curvePointParameter", curvePointParameter);
            return "curvePoint/update";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Updates an existing curve point.
     *
     * @param id                  the ID of the curve point to update
     * @param curvePointParameter the object created after the user fills the form and containing the updated curve point data
     * @param result              the binding result for validation
     * @param model               the model to which the updated list of curve points or error messages are added
     * @return the redirect URL to the curve point list if the update is successful, the update form view if validation fails,
     * or the error view if an exception occurs
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePointParameter curvePointParameter,
                                   BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                curvePointService.updateCurvePoint(id, curvePointParameter);
                model.addAttribute("curvePoints", curvePointService.getAllCurvePoints());
                return "redirect:/curvePoint/list";

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        return "curvePoint/update";
    }

    /**
     * Deletes the targeted curve point.
     *
     * @param id    the ID of the curve point to delete
     * @param model the model to which the updated list of curve points or error messages are added
     * @return the redirect URL to the curve point list if the deletion is successful, or the error view if an exception occurs
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        try {
            curvePointService.deleteCurvePoint(id);
            model.addAttribute("curvePoints", curvePointService.getAllCurvePoints());
            return "redirect:/curvePoint/list";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}