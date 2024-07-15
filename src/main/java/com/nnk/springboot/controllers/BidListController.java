package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.BidListDTO;
import com.nnk.springboot.domain.parameter.BidListParameter;
import com.nnk.springboot.services.BidService;
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
 * Controller for managing Bid Lists.
 */
@Controller
public class BidListController {

    @Autowired
    private BidService bidService;

    /**
     * Displays the list of all bids.
     *
     * @param model the model to which the list of bids is added
     * @return the view name for displaying the bid list
     */
    @RequestMapping("/bidList/list")
    public String home(Model model) {
        List<BidListDTO> bidLists = bidService.getAllBids();
        model.addAttribute("bidListDTOs", bidLists);
        return "bidList/list";
    }

    /**
     * Displays the form for adding a new bid.
     *
     * @param model the model to which a new BidListParameter object is added
     * @return the view name for adding a new bid
     */
    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        model.addAttribute("bidListParameter", new BidListParameter());
        return "bidList/add";
    }

    /**
     * Validates and saves a new bid.
     *
     * @param bidListParameter the parameter object containing the bid data
     * @param result           the binding result for validation errors
     * @param model            the model to which the updated list of bids is added
     * @return the view name to redirect to after saving the bid, or the view name for displaying an error message if an exception occurs
     * or the view name for adding a bid if there are validation errors
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidListParameter bidListParameter, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            try {
                bidService.addBid(bidListParameter);
                model.addAttribute("bidListDTOs", bidService.getAllBids());
                return "redirect:/bidList/list";

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        model.addAttribute("bidListParameter", bidListParameter);
        return "bidList/add";
    }

    /**
     * Displays the form for updating an existing bid.
     *
     * @param id    the ID of the bid to update
     * @param model the model to which the updated list of bids or error messages are added
     * @return the view name for updating a bid, or the view name for displaying an error message if an exception occurs
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            BidListParameter bidListParameter = bidService.getBidListParameterById(id);
            model.addAttribute("bidListParameter", bidListParameter);
            return "bidList/update";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Updates an existing bid.
     *
     * @param id               the ID of the bid to update
     * @param bidListParameter the object created after the user fills the form and containing the updated bid data
     * @param result           the binding result for validation
     * @param model            the model to which the updated list of bids or error messages are added
     * @return the redirect URL to the bid list if the update is successful, the update form view if validation fails,
     * or the error view if an exception occurs
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidListParameter bidListParameter,
                            BindingResult result, Model model) {
        if (!result.hasErrors()) {
            try {
                bidService.updateBidList(id, bidListParameter);
                model.addAttribute("bidListDTOs", bidService.getAllBids());
                return "redirect:/bidList/list";

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        return "bidList/update";
    }

    /**
     * Deletes the targeted bid.
     *
     * @param id    the ID of the bid to delete
     * @param model the model to which the updated list of bids or error messages are added
     * @return the redirect URL to the bid list if the deletion is successful, or the error view if an exception occurs
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        try {
            bidService.deleteBid(id);
            model.addAttribute("bidLists", bidService.getAllBids());
            return "redirect:/bidList/list";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}
