package com.github.sysimp.controllers;


import com.github.sysimp.exceptions.NotFoundException;
import com.github.sysimp.model.Rate;
import com.github.sysimp.repositories.RateRepository;
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
    private RateRepository rateRepository;

    @GetMapping
    public ArrayList<Rate> showRates() {
        return (ArrayList<Rate>) rateRepository.findAll();
    }

    @GetMapping("{id}")
    public Rate showRatesById(@PathVariable("id") Rate rate) {
        if (rate == null)
            throw new NotFoundException();

        return rate;
    }

}
