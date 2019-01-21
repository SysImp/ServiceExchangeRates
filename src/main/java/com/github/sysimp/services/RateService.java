package com.github.sysimp.services;

import com.github.sysimp.model.Rate;

import java.util.List;

public interface RateService {

    Rate addRate(Rate rate);
    void delete(long id);
    Rate getById(long id);
    Rate editRate(Rate rate);
    List<Rate> getAll();
}
