package com.github.sysimp.controllers;


import com.github.sysimp.exceptions.NotFoundException;
import com.github.sysimp.entity.Rate;
import com.github.sysimp.services.RateService;
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
    private RateService rateService;

    @GetMapping
    public ArrayList<Rate> showRates() {
        return (ArrayList<Rate>) rateService.getAll();
    }

    @GetMapping("{id}")
    public Rate showRatesById(@PathVariable("id") Rate rate) {
        if (rate == null)
            throw new NotFoundException();

        return rate;
    }

}
