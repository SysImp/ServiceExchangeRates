package com.github.sysimp.services.impls;

import com.github.sysimp.model.Rate;
import com.github.sysimp.repositories.RateRepository;
import com.github.sysimp.services.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
public class RateServiceImpl implements RateService {

    @Autowired
    private RateRepository rateRepository;

    @Override
    public Rate addRate(Rate rate) {
        return rateRepository.saveAndFlush(rate);
    }

    @Override
    public void delete(long id) {
        rateRepository.deleteById(id);
    }

    @Override
    public Rate getById(long id) {
        return rateRepository.getById(id);
    }

    @Override
    public Rate editRate(Rate rate) {
        return rateRepository.saveAndFlush(rate);
    }

    @Override
    public List<Rate> getAll() {
        return rateRepository.findAll();
    }

    @Scheduled(fixedDelay = 1800000, initialDelay = 1000)
    public void updateRates() {
        System.out.println("update rates...");
    }
}
