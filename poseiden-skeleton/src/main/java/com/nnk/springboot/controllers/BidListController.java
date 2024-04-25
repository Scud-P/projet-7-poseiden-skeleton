package com.nnk.springboot.controllers;

import com.nnk.springboot.configuration.SpringSecurityConfig;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
public class BidListController {

    @Autowired
    private BidService bidService;
    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        List<BidList> bidLists = bidService.getAllBids();
        model.addAttribute("bidLists", bidLists);
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {

        if(!result.hasErrors()) {
            bidService.addBid(bid);
            model.addAttribute("bidLists", bidService.getAllBids());
            return "redirect:/bidList/list";
        }

        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bidToUpdate = bidService.getBidById(id);
        model.addAttribute("bidList", bidToUpdate);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Bid and return list Bid

        if(!result.hasErrors()) {
            bidService.updateBidList(id, bidList);
            model.addAttribute("bidLists", bidService.getAllBids());
            return "redirect:/bidList/list";
        }
        return "bidList/update";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        BidList bidToDelete = bidService.getBidById(id);
        bidService.deleteBid(bidToDelete);
        model.addAttribute("bidLists", bidService.getAllBids());
        return "redirect:/bidList/list";
    }
}
