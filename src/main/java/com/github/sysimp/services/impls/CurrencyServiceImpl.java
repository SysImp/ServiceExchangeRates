package com.github.sysimp.services.impls;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sysimp.entity.Currency;
import com.github.sysimp.entity.Rate;
import com.github.sysimp.repositories.CurrencyRepository;
import com.github.sysimp.services.CurrencyService;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public Currency addCurrency(Currency currency) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Currency getById(long id) {
        return null;
    }

    @Override
    public Currency editCurrency(Currency rate) {
        return null;
    }

    @Override
    public List<Currency> getAll() {
        return null;
    }

    @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    public void updateCurrency() {

        for (Currency currency : currencyRepository.findAll()) {
            currency.setValue(getActualCurrency(currency.getName()));
            currencyRepository.saveAndFlush(currency);
        }
    }

    public void loadAllCurrencyFromXML() {
        DOMParser parser = new DOMParser();

    }

    public boolean isLoadedCurrency(String nameCurrency) {
        if (currencyRepository.getByName(nameCurrency) == null)
            return false;

        return true;
    }

    public double getActualCurrency(String nameCurrency) {
        double value = -1;
        try(
                CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = client.execute(getHttpResponse(nameCurrency));
        ) {
            InputStream inputStream = response.getEntity().getContent();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jNode = objectMapper.readTree(inputStream);

            jNode = jNode.findPath(String.format("%s_USD", nameCurrency));
            Rate rate = objectMapper.readValue(jNode.toString(), Rate.class);

            value = rate.getValue();
        }
        catch (Throwable cause) {
            cause.printStackTrace();
        }

        return value;
    }

    public HttpGet getHttpResponse(String nameCurrency) {
        return new HttpGet(String.format("https://free.currencyconverterapi.com/api/v6/convert?q=%s_USD", nameCurrency));
    }
}
