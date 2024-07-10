package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.BidListDTO;
import com.nnk.springboot.domain.parameter.BidListParameter;
import com.nnk.springboot.services.BidService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BidListController {

    @Autowired
    private BidService bidService;

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        List<BidListDTO> bidLists = bidService.getAllBids();
        model.addAttribute("bidListDTOs", bidLists);
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        model.addAttribute("bidListParameter", new BidListParameter());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidListParameter bidListParameter, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            bidService.addBid(bidListParameter);
            model.addAttribute("bidListDTOs", bidService.getAllBids());
            return "redirect:/bidList/list";
        }
        model.addAttribute("bidListParameter", bidListParameter);
        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidListParameter bidListParameter = bidService.getBidListParameterById(id);
        model.addAttribute("bidListParameter", bidListParameter);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidListParameter bidListParameter,
                            BindingResult result, Model model) {
        if (!result.hasErrors()) {
            bidService.updateBidList(id, bidListParameter);
            model.addAttribute("bidListDTOs", bidService.getAllBids());
            return "redirect:/bidList/list";
        }
        return "bidList/update";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        bidService.deleteBid(id);
        model.addAttribute("bidLists", bidService.getAllBids());
        return "redirect:/bidList/list";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
