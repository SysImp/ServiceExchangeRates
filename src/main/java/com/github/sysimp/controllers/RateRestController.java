package com.github.sysimp.controllers;


import com.github.sysimp.entities.Currency;
import com.github.sysimp.entities.Rate;
import com.github.sysimp.exceptions.NotFoundException;
import com.github.sysimp.services.CurrencyService;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "rest/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RateRestController {

    private CurrencyService currencyService;

    private RateRestController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("rate")
    public List<Currency> showRates() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return currencyService.getAll(sort);
    }

    @GetMapping("rate/{id}")
    public Rate showRatesById(@PathVariable("id") Currency currency) {
        if (currency == null) {
            throw new NotFoundException();
        }
        return new Rate(currency.getValue(), currency.getName(), "USD", LocalDateTime.now());
    }

    @GetMapping("/get/{request}")
    public Rate showRatesByRequest(@PathVariable("request") String request) {
        Rate rate = currencyService.getRate(request);
        if (rate == null) {
            throw new NotFoundException();
        }
        return rate;
    }

    @GetMapping("update")
    public List<Currency> updateCurrencies() {
        currencyService.updateCurrencies();
        return currencyService.getAll();
    }

    @GetMapping("updateXML")
    public List<Currency> updateCurrenciesFromXML() {
        currencyService.updateAllCurrencyFromXml();
        return currencyService.getAll();
    }
}
