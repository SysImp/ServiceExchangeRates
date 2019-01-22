package com.github.sysimp.services;

import com.github.sysimp.entity.Currency;

import java.util.List;

public interface CurrencyService {

    Currency addCurrency(Currency currency);
    void delete(long id);
    Currency getById(long id);
    Currency editCurrency(Currency rate);
    List<Currency> getAll();
}
