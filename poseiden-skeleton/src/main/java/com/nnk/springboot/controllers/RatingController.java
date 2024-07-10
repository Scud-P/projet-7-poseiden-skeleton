package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.RatingDTO;
import com.nnk.springboot.domain.parameter.RatingParameter;
import com.nnk.springboot.services.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class RatingController {

    @Autowired
    private RatingService ratingService;


    @RequestMapping("/rating/list")
    public String home(Model model) {
        List<RatingDTO> ratingDTOs = ratingService.getAllRatings();
        model.addAttribute("ratingDTOs", ratingDTOs);
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        model.addAttribute("ratingParameter", new RatingParameter());
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid RatingParameter ratingParameter, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            ratingService.addRating(ratingParameter);
            model.addAttribute("ratingDTOs", ratingService.getAllRatings());
            return "redirect:/rating/list";
        }
        model.addAttribute("ratingParameter", ratingParameter);
        return "rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RatingParameter ratingParameter = ratingService.getRatingParameterById(id);
        model.addAttribute("ratingParameter", ratingParameter);
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid RatingParameter ratingParameter,
                               BindingResult result, Model model) {

        if (!result.hasErrors()) {
            ratingService.updateRating(id, ratingParameter);
            model.addAttribute("ratings", ratingService.getAllRatings());
            return "redirect:/rating/list";
        }
        return "rating/update";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        ratingService.deleteRating(id);
        model.addAttribute("ratings", ratingService.getAllRatings());
        return "redirect:/rating/list";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
