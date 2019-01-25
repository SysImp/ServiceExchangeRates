package com.github.sysimp.controllers;

import com.github.sysimp.entity.Rate;
import com.github.sysimp.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RateController {

    @Autowired
    private CurrencyService currencyService;

    @RequestMapping(value = {"/main", "/"}, method = RequestMethod.GET)
    public String showMain(Model model) {
        Sort sort = new Sort(Sort.Direction.ASC, "name");
        model.addAttribute("listAllowCurrencies", currencyService.getAll(sort));


        return "main";
    }

    @RequestMapping(value = {"/combs"}, method = RequestMethod.GET)
    public Model combs(Model model) {
        model.addAttribute("listCombs", currencyService.getCombsRate());


        return model;
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
    public Model searchGET(Model model) {
        model.addAttribute("allowRequest", currencyService.getAllowCurrencies());


        return model;
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.POST)
    public Model searchPOST(@RequestParam String request, Model model) {
        Rate rate = currencyService.getRate(request);
        model.addAttribute("allowRequest", currencyService.getAllowCurrencies());

        if (rate != null) {
            model.addAttribute("value", rate.getValue());
            model.addAttribute("request", rate.getFromCurrency() + "_" + rate.getToCurrency());
        } else {
            model.addAttribute("value", "Bad request: " + request);
            model.addAttribute("request", "Bad request: " + request);
        }

        return model;
    }

}
