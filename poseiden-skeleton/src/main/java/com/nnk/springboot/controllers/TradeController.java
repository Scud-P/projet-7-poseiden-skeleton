package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        List<Trade> tradeList = tradeService.getAllTrades();
        model.addAttribute("tradeList", tradeList);
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addTrade(Trade trade) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade tradeToAdd, BindingResult result, Model model) {

        if(!result.hasErrors()) {
            tradeService.addTrade(tradeToAdd);
            model.addAttribute("tradeList", tradeService.getAllTrades());
            return "redirect:/trade/list";
        }
        // TODO: check data valid and save to db, after saving return Trade list
        return "trade/add";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade tradeToUpdate = tradeService.getTradeById(id);
        model.addAttribute("trade", tradeToUpdate);
        // TODO: get Trade by Id and to model then show to the form
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {

        if(!result.hasErrors()) {
            tradeService.updateTrade(id, trade);
            model.addAttribute("tradeList", tradeService.getAllTrades());
            return "redirect:/trade/list";
        }
        return "trade/update";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        tradeService.deleteTrade(id);
        model.addAttribute("tradeList", tradeService.getAllTrades());
        // TODO: Find Trade by Id and delete the Trade, return to Trade list
        return "redirect:/trade/list";
    }
}
