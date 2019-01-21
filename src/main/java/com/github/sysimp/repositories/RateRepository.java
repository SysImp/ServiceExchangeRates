package com.github.sysimp.repositories;

import com.github.sysimp.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, Long> {

    Rate getById(long ind);
}
