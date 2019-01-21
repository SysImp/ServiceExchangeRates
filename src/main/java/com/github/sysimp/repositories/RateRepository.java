package com.github.sysimp.repositories;

import com.github.sysimp.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, Long> {

    Rate getById(long id);
    void deleteById(long id);
}
