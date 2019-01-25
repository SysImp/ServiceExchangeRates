package com.github.sysimp.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.sysimp.entity.Currency;
import com.github.sysimp.entity.Rate;
import com.github.sysimp.repositories.CurrencyRepository;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class CurrencyService {

    //%s - name currency
    private static String ADDRESS_API = "https://free.currencyconverterapi.com/api/v6/convert?q=%s_USD";
    private static String PATH_XML_TEMPLATE = "xml/currnecyTemplate.xml";

    private CurrencyRepository currencyRepository;


    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;

    }

    public void createCurrency(Currency currency) {
        currencyRepository.save(currency);
    }

    public void editCurrency(Currency currency) {
        currencyRepository.save(currency);
    }

    public void updateCurrency(Currency currency) {
        currency.setValue(getActualCurrency(currency.getName()));
        currencyRepository.saveAndFlush(currency);
    }

    public void updateCurrencyByTemplate(Currency currency, TemplateCurrency template) {
        currency.setId(template.getId());
        currency.setDescription(template.getDescription());
        currency.setValue(getActualCurrency(currency.getName()));
        currencyRepository.saveAndFlush(currency);
    }

    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    public List<Currency> getAll(Sort sort) {
        return currencyRepository.findAll(sort);
    }

    public ArrayList<String> getAllowCurrencies() {
        ArrayList<String> list = new ArrayList<>();
        for (Currency currency : getAll())
            list.add(currency.getName());

        return list;
    }

    @Scheduled(fixedDelay = 20000, initialDelay = 1000)
    public void updateService() {
        //updateCurrencies();
        //updateAllCurrencyFromXML();
    }

    public ArrayList<Rate> getCombsRate() {
        ArrayList<Rate> listRate = new ArrayList<>();
        List<Currency> listAllCurrencies = getAll();
        //Combs
        for (int i = 0; i < listAllCurrencies.size(); i++)
            for (int j = 0; j < listAllCurrencies.size(); j++)
                listRate.add(getRate(createRequestForRate(listAllCurrencies.get(i), listAllCurrencies.get(j))));

        return listRate;
    }

    public String createRequestForRate(Currency currencyFrom, Currency currencyTo) {
        return String.format("%s_%s", currencyFrom.getName(), currencyTo.getName());
    }

    public String createRequestForRate(String from, String to) {
        return String.format("%s_%s", from, to);
    }

    public boolean checkRequest(String request) {
        if (request.length() != 7)
            return false;

        //args[0] - from, args[1] - to
        String[] args = request.split("_");

        if (args.length != 2)
            return false;


        return true;
    }

    public Rate getRate(String request) {
        if (!checkRequest(request))
            return null;

        String[] args = request.split("_");

        //args[0] == args[1]: ex: USD_USD and etc...
        if (args[0].equalsIgnoreCase(args[1]))
            return new Rate(1, args[0], args[1], LocalDateTime.now());

        return getRate(args[0], args[1]);
    }

    private Rate getRate(String from, String to) {
        Currency currencyFrom = currencyRepository.getByName(from);
        Currency currencyTo = currencyRepository.getByName(to);

        if (currencyFrom == null || currencyTo == null)
            return null;

        return new Rate(currencyFrom.getValue() / currencyTo.getValue(), currencyFrom.getName(), currencyTo.getName(), LocalDateTime.now());
    }

    public void updateAllCurrencyFromXML() {
        XmlMapper xmlMapper = new XmlMapper();
        TemplateXMLCurrencies templateXMLCurrencies = null;
        try {
            InputStream inputStreamFile = new ClassPathResource(PATH_XML_TEMPLATE).getInputStream();
            String xmlText = IOUtils.toString(inputStreamFile, "UTF-8");
            templateXMLCurrencies = xmlMapper.readValue(xmlText, TemplateXMLCurrencies.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (templateXMLCurrencies != null)
            createNewCurrency(templateXMLCurrencies);
    }

    private Currency getCurrencyByTemplate(TemplateCurrency template) {
        return new Currency(template.getId(), template.getName(),
                template.getDescription(), getActualCurrency(template.getName()), LocalDateTime.now());
    }

    private void createNewCurrency(TemplateXMLCurrencies templateXMLCurrencies) {
        for (TemplateCurrency tCurrency : templateXMLCurrencies.getCurrencies()) {
            Currency currency = currencyRepository.getByName(tCurrency.getName());
            if (currency == null)
                createCurrency(getCurrencyByTemplate(tCurrency));
            else
                updateCurrencyByTemplate(currency, tCurrency);
        }
    }

    public void updateCurrencies() {
        for (Currency currency : getAll()) {
            double newValue = getActualCurrency(currency.getName());
            if (newValue != -1)
                currency.setValue(newValue);
            editCurrency(currency);
        }
    }

    private double getActualCurrency(String nameCurrency) {
        double value = -1;
        try (CloseableHttpClient client = HttpClients.createDefault(); CloseableHttpResponse response = client.execute(getHttpResponse(nameCurrency))) {
            InputStream inputStream = response.getEntity().getContent();
            String jText = IOUtils.toString(inputStream, "UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jNode = objectMapper.readTree(jText);

            jNode = jNode.findPath(String.format("%s_USD", nameCurrency));
            Rate rate = objectMapper.readValue(jNode.toString(), Rate.class);
            value = rate.getValue();
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return value;
    }

    private HttpGet getHttpResponse(String nameCurrency) {
        return new HttpGet(String.format(ADDRESS_API, nameCurrency));
    }


}
