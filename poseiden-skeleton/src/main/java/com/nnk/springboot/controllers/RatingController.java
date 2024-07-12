package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.RatingDTO;
import com.nnk.springboot.domain.parameter.RatingParameter;
import com.nnk.springboot.services.RatingService;
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
 * Controller for managing Ratings.
 */
@Controller
public class RatingController {

    @Autowired
    private RatingService ratingService;

    /**
     * Displays the list ratings.
     *
     * @param model the model to which the list of ratings is added
     * @return the view name for displaying the ratings
     */
    @RequestMapping("/rating/list")
    public String home(Model model) {
        List<RatingDTO> ratingDTOs = ratingService.getAllRatings();
        model.addAttribute("ratingDTOs", ratingDTOs);
        return "rating/list";
    }

    /**
     * Displays the form for adding a new rating
     *
     * @param model the model to which a new RatingParameter object is added
     * @return the view name for adding a new rating
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        model.addAttribute("ratingParameter", new RatingParameter());
        return "rating/add";
    }

    /**
     * Validates and saves a new rating
     *
     * @param ratingParameter the parameter object containing the rating data
     * @param result          the binding result for validation errors
     * @param model           the model to which the updated list of ratings is added
     * @return the view name to redirect to after saving the rating, or the view name for adding a rating if there are validation errors
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid RatingParameter ratingParameter, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                ratingService.addRating(ratingParameter);
                model.addAttribute("ratingDTOs", ratingService.getAllRatings());
                return "redirect:/rating/list";

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        model.addAttribute("ratingParameter", ratingParameter);
        return "rating/add";
    }

    /**
     * Displays the form for updating an existing rating.
     *
     * @param id    the ID of the rating to update
     * @param model the model to which the updated list of ratings or error messages are added
     * @return the view name for updating a rating, or the view name for displaying an error message if an exception occurs
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            RatingParameter ratingParameter = ratingService.getRatingParameterById(id);
            model.addAttribute("ratingParameter", ratingParameter);
            return "rating/update";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Updates an existing rating.
     *
     * @param id              the ID of the rating to update
     * @param ratingParameter the object created after the user fills the form and containing the updated rating data
     * @param result          the binding result for validation
     * @param model           the model to which the updated list of ratings or error messages are added
     * @return the redirect URL to the rating list if the update is successful, the update form view if validation fails,
     * or the error view if an exception occurs
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid RatingParameter ratingParameter,
                               BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                ratingService.updateRating(id, ratingParameter);
                model.addAttribute("ratings", ratingService.getAllRatings());
                return "redirect:/rating/list";

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        return "rating/update";
    }

    /**
     * Deletes the targeted rating.
     *
     * @param id    the ID of the rating to delete
     * @param model the model to which the updated list of ratings or error messages are added
     * @return the redirect URL to the rating list if the deletion is successful, or the error view if an exception occurs
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {

        try {
            ratingService.deleteRating(id);
            model.addAttribute("ratings", ratingService.getAllRatings());
            return "redirect:/rating/list";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}
