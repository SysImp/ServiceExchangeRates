package com.github.sysimp.controllers;

import com.github.sysimp.entities.Rate;
import com.github.sysimp.services.CurrencyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class RateController {

    private CurrencyService currencyService;

    public RateController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String showMain(Model model) {
        model.addAttribute("AllowCurrencies", currencyService.getAllowCurrencies());
        model.addAttribute("value", 0);
        model.addAttribute("count", 1);

        return "index";
    }

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.POST)
    public String searchMain(@RequestParam String count, @RequestParam String from, @RequestParam String to, Model model) {
        List<String> list = currencyService.getAllowCurrencies();
        Collections.sort(list);

        model.addAttribute("AllowCurrencies", list);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        int c = 1;
        try {
            c = Integer.parseInt(count);
        } catch (NumberFormatException e) {
        }

        model.addAttribute("count", c);

        Rate rate = currencyService.getRate(currencyService.createRequestForRate(from, to));
        model.addAttribute("value", rate.getValue() * c);

        return "index";
    }

    @RequestMapping(value = {"/combs"}, method = RequestMethod.GET)
    public Model combs(Model model) {
        model.addAttribute("listCombs", currencyService.getListCombsRate());
        model.addAttribute("countCurrency", currencyService.getAll().size());

        return model;
    }


}