package com.github.sysimp.repositories;

import com.github.sysimp.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long> {

    Rate getById(long ind);
    List<Rate> getAllByIdNotNull();
}
