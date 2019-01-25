package com.github.sysimp.controllers;


import com.github.sysimp.entity.Currency;
import com.github.sysimp.entity.Rate;
import com.github.sysimp.exceptions.NotFoundException;
import com.github.sysimp.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "rest/rate", produces= MediaType.APPLICATION_JSON_VALUE)
public class RateRestController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public ArrayList<Currency> showRates() {
        return (ArrayList<Currency>) currencyService.getAll();
    }

    @GetMapping("{id}")
    public Rate showRatesById(@PathVariable("id") Currency currency) {
        if (currency == null)
            throw new NotFoundException();

        Rate rate = new Rate(currency.getValue(), currency.getName(), "USD", LocalDateTime.now());

        return rate;
    }

    @PostMapping("/get/{request}")
    public String showRatesByRequest(@PathVariable("request") String request) {
        Rate rate = currencyService.getRate(request);

        if (rate == null)
            return "Error! Bad request!";

        return rate.toString();

    }

    @GetMapping("update")
    public List<Currency> updateCurrencies() {
        currencyService.updateCurrencies();
        return currencyService.getAll();
    }

    @GetMapping("updateXML")
    public List<Currency> updateCurrenciesFromXML() {
        currencyService.updateAllCurrencyFromXML();
        return currencyService.getAll();
    }
}
