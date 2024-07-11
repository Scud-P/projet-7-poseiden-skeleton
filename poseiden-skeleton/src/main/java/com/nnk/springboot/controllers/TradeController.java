package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.TradeDTO;
import com.nnk.springboot.domain.parameter.TradeParameter;
import com.nnk.springboot.services.TradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model) {
        List<TradeDTO> tradeDTOs = tradeService.getAllTrades();
        model.addAttribute("tradeDTOs", tradeDTOs);
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addTrade(Model model) {
    model.addAttribute("tradeParameter", new TradeParameter());
    return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid TradeParameter tradeParameter, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            tradeService.addTrade(tradeParameter);
            model.addAttribute("tradeDTOs", tradeService.getAllTrades());
            return "redirect:/trade/list";
        }
        model.addAttribute("tradeParameter", tradeParameter);
        return "trade/add";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        TradeParameter tradeParameter = tradeService.getTradeParameterById(id);
        model.addAttribute("tradeParameter", tradeParameter);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid TradeParameter tradeParameter,
                              BindingResult result, Model model) {

        if (!result.hasErrors()) {
            tradeService.updateTrade(id, tradeParameter);
            model.addAttribute("tradeDTOs", tradeService.getAllTrades());
            return "redirect:/trade/list";
        }
        return "trade/update";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        tradeService.deleteTrade(id);
        model.addAttribute("trades", tradeService.getAllTrades());
        return "redirect:/trade/list";
    }
}
