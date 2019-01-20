package com.github.sysimp.controllers;

import com.github.sysimp.repositories.RateRepository;
import com.github.sysimp.model.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@RestController
public class RateController {

    @Autowired
    private RateRepository rateRepository;

    @RequestMapping(value = "/getRates", method = RequestMethod.GET)
    public ArrayList<Rate> showRates() {
        return (ArrayList<Rate>) rateRepository.getAllByIdNotNull();
    }

    @RequestMapping(value = "/getRate", method = RequestMethod.GET)
    public Rate showRatesById(@RequestParam(value = "id") int id) {
        return rateRepository.getById(id);
    }
}
