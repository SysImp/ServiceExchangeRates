package com.github.sysimp.controllers;


import com.github.sysimp.entity.Currency;
import com.github.sysimp.exceptions.NotFoundException;
import com.github.sysimp.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "rate")
public class RateController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public ArrayList<Currency> showRates() {
        return (ArrayList<Currency>) currencyService.getAll();
    }

    @GetMapping("{id}")
    public Currency showRatesById(@PathVariable("id") Currency rate) {
        if (rate == null)
            throw new NotFoundException();

        return rate;
    }

}
