package com.github.sysimp.repositories;

import com.github.sysimp.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency getById(long id);
    Currency getByName(String name);
}
