package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DTO.TradeDTO;
import com.nnk.springboot.domain.parameter.TradeParameter;
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

/**
 * Controller for managing Trades.
 */
@Controller
public class TradeController {

    @Autowired
    private TradeService tradeService;

    /**
     * Displays the list trades.
     *
     * @param model the model to which the list of trades is added
     * @return the view name for displaying the trades
     */
    @RequestMapping("/trade/list")
    public String home(Model model) {
        List<TradeDTO> tradeDTOs = tradeService.getAllTrades();
        model.addAttribute("tradeDTOs", tradeDTOs);
        return "trade/list";
    }

    /**
     * Displays the form for adding a new trade
     *
     * @param model the model to which a new TradeParameter object is added
     * @return the view name for adding a new trade
     */
    @GetMapping("/trade/add")
    public String addTrade(Model model) {
        model.addAttribute("tradeParameter", new TradeParameter());
        return "trade/add";
    }

    /**
     * Validates and saves a new trade
     *
     * @param tradeParameter the parameter object containing the trade data
     * @param result         the binding result for validation errors
     * @param model          the model to which the updated list of trades is added
     * @return the view name to redirect to after saving the trade, or the view name for adding a trade if there are validation errors
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid TradeParameter tradeParameter, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                tradeService.addTrade(tradeParameter);
                model.addAttribute("tradeDTOs", tradeService.getAllTrades());
                return "redirect:/trade/list";

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        model.addAttribute("tradeParameter", tradeParameter);
        return "trade/add";
    }

    /**
     * Displays the form for updating an existing trade.
     *
     * @param id    the ID of the trade to update
     * @param model the model to which the updated list of trades or error messages are added
     * @return the view name for updating a trade, or the view name for displaying an error message if an exception occurs
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            TradeParameter tradeParameter = tradeService.getTradeParameterById(id);
            model.addAttribute("tradeParameter", tradeParameter);
            return "trade/update";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * Updates an existing trade.
     *
     * @param id             the ID of the trade to update
     * @param tradeParameter the object created after the user fills the form and containing the updated trade data
     * @param result         the binding result for validation
     * @param model          the model to which the updated list of trades or error messages are added
     * @return the redirect URL to the trade list if the update is successful, the update form view if validation fails,
     * or the error view if an exception occurs
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid TradeParameter tradeParameter,
                              BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                tradeService.updateTrade(id, tradeParameter);
                model.addAttribute("tradeDTOs", tradeService.getAllTrades());
                return "redirect:/trade/list";

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        return "trade/update";
    }

    /**
     * Deletes the targeted trade.
     *
     * @param id    the ID of the trade to delete
     * @param model the model to which the updated list of trades or error messages are added
     * @return the redirect URL to the trade list if the deletion is successful, or the error view if an exception occurs
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        try {
            tradeService.deleteTrade(id);
            model.addAttribute("trades", tradeService.getAllTrades());
            return "redirect:/trade/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}
