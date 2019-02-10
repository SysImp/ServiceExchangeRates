package com.github.sysimp.repositories;

import com.github.sysimp.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency getByName(String name);
}
