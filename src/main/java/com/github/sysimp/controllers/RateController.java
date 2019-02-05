package com.github.sysimp.controllers;

import com.github.sysimp.entities.Currency;
import com.github.sysimp.entities.Rate;
import com.github.sysimp.exceptions.NotFoundException;
import com.github.sysimp.services.CurrencyService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.regex.Pattern;

@Controller
public class RateController {

    private CurrencyService currencyService;

    public RateController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String showMain(Model model) {
        List<Currency> list = currencyService.getAll(new Sort(Sort.Direction.ASC, "name"));
        model.addAttribute("AllowCurrencies", list);
        if (!list.isEmpty()) {
            model.addAttribute("from", list.get(0));
            model.addAttribute("to", list.get(0));
        }
        model.addAttribute("value", 0);
        model.addAttribute("count", 1);

        return "index";
    }

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.POST)
    public String searchMain(@RequestParam String count, @RequestParam String from, @RequestParam String to, Model model) {
        if (!checkParam(from, to, count))
            throw new NotFoundException();

        Rate rate = currencyService.getRate(currencyService.createRequestForRate(from, to));
        int multiplier = parseInt(count);

        List<Currency> list = currencyService.getAll(new Sort(Sort.Direction.ASC, "name"));

        model.addAttribute("AllowCurrencies", list);
        model.addAttribute("from", currencyService.getCurrencyByName(from));
        model.addAttribute("to", currencyService.getCurrencyByName(to));
        model.addAttribute("count", multiplier);
        model.addAttribute("value", rate.getValue() * multiplier);

        return "index";
    }

    @RequestMapping(value = {"/combs"}, method = RequestMethod.GET)
    public Model showCombs(Model model) {
        model.addAttribute("listCombs", currencyService.getListCombsRate());
        model.addAttribute("countCurrency", currencyService.getAll().size());

        return model;
    }

    @RequestMapping(value = {"/currencies"}, method = RequestMethod.GET)
    public Model showCurrencies(Model model) {
        model.addAttribute("listCurrnecies", currencyService.getAll(new Sort(Sort.Direction.ASC, "id")));
        return model;
    }

    private boolean checkParam(String from, String to, String count) {
        List<String> allowCurrencies = currencyService.getAllowCurrencies();
        if (!(allowCurrencies.contains(from) && allowCurrencies.contains(to))) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(count).matches();
    }

    private int parseInt(String param) {
        try {
            return Integer.parseInt(param);
        } catch (Exception e) {
            return 1;
        }
    }
}