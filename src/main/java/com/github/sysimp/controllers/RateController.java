package com.github.sysimp.controllers;

import com.github.sysimp.controllers.validator.ExchangeForm;
import com.github.sysimp.controllers.validator.ExchangeFormValidator;
import com.github.sysimp.entities.Currency;
import com.github.sysimp.entities.Rate;
import com.github.sysimp.services.CurrencyService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class RateController {

    private CurrencyService currencyService;
    private ExchangeFormValidator exchangeFormValidator;

    public RateController(CurrencyService currencyService, ExchangeFormValidator exchangeFormValidator) {
        this.currencyService = currencyService;
        this.exchangeFormValidator = exchangeFormValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(exchangeFormValidator);
    }

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String showMain(Model model) {
        defaultModel(model);
        ExchangeForm exchangeForm = new ExchangeForm();
        model.addAttribute("exchangeForm", exchangeForm);

        return "index";
    }

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.POST)
    public String searchMain(@ModelAttribute("exchangeForm") @Validated ExchangeForm exchangeForm, BindingResult result, Model model) {
        defaultModel(model);
        if (!result.hasErrors()) {
            String request = currencyService.createRequestForRate(exchangeForm.getFromCurrency(), exchangeForm.getToCurrency());
            int factor = parseInt(exchangeForm.getFactor());

            Rate rate = currencyService.getRate(request);
            exchangeForm.setValue(rate.getValue() * factor);
        }
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

    private void defaultModel(Model model) {
        List<Currency> list = currencyService.getAll(new Sort(Sort.Direction.ASC, "name"));
        model.addAttribute("AllowCurrencies", list);
    }

    private int parseInt(String param) {
        try {
            return Integer.parseInt(param);
        } catch (Exception e) {
            return 1;
        }
    }
}