package com.github.sysimp.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.sysimp.entities.Currency;
import com.github.sysimp.entities.Rate;
import com.github.sysimp.repositories.CurrencyRepository;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.logging.log4j.MarkerManager.getMarker;

@Service
@EnableScheduling
public class CurrencyService {

    private static Logger logger = LogManager.getLogger(CurrencyService.class);

    //%s - name currency
    private static final String ADDRESS_API = "https://free.currencyconverterapi.com/api/v6/convert?q=%s_USD";
    private static final String PATH_XML_TEMPLATE = "static/xml/currencyTemplate.xml";

    private CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
        logger.info(getMarker("services"), "Constructor: CurrencyService(CurrencyRepository currencyRepository)");
    }

    public void createCurrency(Currency currency) {
        currencyRepository.save(currency);

        logger.info(getMarker("services"), String.format("createCurrency: %s", currency));
    }

    public void editCurrency(Currency currency) {
        currencyRepository.save(currency);

        logger.info(getMarker("services"), String.format("editCurrency: %s", currency));
    }

    public void updateCurrency(Currency currency, TemplateCurrency template) {
        currency.setId(template.getId());
        currency.setDescription(template.getDescription());

        currencyRepository.saveAndFlush(currency);

        logger.info(getMarker("services"), String.format("updateCurrency: %s, template:%s", currency, template));
    }

    public Currency getCurrencyByTemplate(TemplateCurrency template) {
        return new Currency(template.getId(), template.getName(), template.getDescription(), -1, LocalDateTime.now());
    }

    public void deleteCurrency(Currency currency) {
        currencyRepository.delete(currency);

        logger.info(getMarker("services"), String.format("deleteCurrency: %s", currency));
    }

    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    public List<Currency> getAll(Sort sort) {
        return currencyRepository.findAll(sort);
    }

    public List<String> getAllowCurrencies() {
        List<String> list = new ArrayList<>();

        for (Currency currency : getAll()) {
            list.add(currency.getName());
        }

        return list;
    }

    public List<Rate> getListCombsRate() {
        List<Currency> listAllCurrencies = getAll();
        List<Rate> list = new ArrayList<>();
        //Combs
        for (int i = 0; i < listAllCurrencies.size(); i++) {
            for (int j = 0; j < listAllCurrencies.size(); j++) {
                list.add(getRate(createRequestForRate(listAllCurrencies.get(i), listAllCurrencies.get(j))));
            }
        }

        return list;
    }

    @Scheduled(fixedRate = 1800000)
    public void updateService() {
        logger.info(getMarker("services"), "updateService() fixedRate = 1800000");
        updateCurrencies();
    }

    public String createRequestForRate(Currency currencyFrom, Currency currencyTo) {
        return createRequestForRate(currencyFrom.getName(), currencyTo.getName());
    }

    public String createRequestForRate(String from, String to) {
        logger.info(getMarker("services"), String.format("createRequestForRate(String from = %s, String to = %s)", from, to));
        return String.format("%s_%s", from, to);
    }

    public boolean checkRequest(String request) {
        if (request.length() != 7) {
            return false;
        }
        //args[0] - from, args[1] - to
        String[] args = request.split("_");

        if (args.length != 2) {
            return false;
        }

        return true;
    }

    public Rate getRate(String request) {
        if (!checkRequest(request)) {
            return null;
        }

        String[] args = request.split("_");

        //args[0] == args[1]: ex: USD_USD and etc...
        if (args[0].equalsIgnoreCase(args[1])) {
            return new Rate(1, args[0], args[1], LocalDateTime.now());
        }

        return getRate(args[0], args[1]);
    }

    public void updateAllCurrencyFromXml(InputStream inputStreamFile) {
        logger.info(getMarker("services"), "START: updateAllCurrencyFromXml");
        try {
            String xmlText = IOUtils.toString(inputStreamFile, "UTF-8");
            TemplateXMLCurrencies templateXMLCurrencies = getTemplatesXMLCurrencies(xmlText);
            updateAllCurrencyFromXML(templateXMLCurrencies);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        logger.info(getMarker("services"), "FINISH: updateAllCurrencyFromXml");
    }

    public void updateAllCurrencyFromXml() {
        try (InputStream inputStreamFile = new ClassPathResource(PATH_XML_TEMPLATE).getInputStream()) {
            updateAllCurrencyFromXml(inputStreamFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void updateCurrencies() {
        logger.info(getMarker("services"), "START: updateCurrencies");
        for (Currency currency : getAll()) {
            double newValue = getActualCurrency(currency.getName());
            if (newValue != -1) {
                currency.setValue(newValue);
            }
            editCurrency(currency);
        }
        logger.info(getMarker("services"), "FINISH: updateCurrencies");
    }

    private Rate getRate(String from, String to) {
        Currency currencyFrom = currencyRepository.getByName(from);
        Currency currencyTo = currencyRepository.getByName(to);

        if (currencyFrom == null || currencyTo == null) {
            return null;
        }

        return new Rate(currencyFrom.getValue() / currencyTo.getValue(), currencyFrom.getName(), currencyTo.getName(), currencyFrom.getLastUpdate());
    }

    private void updateAllCurrencyFromXML(TemplateXMLCurrencies templateXMLCurrencies) {
        if (templateXMLCurrencies != null) {
            deleteOldCurrency(templateXMLCurrencies);
            createNewCurrency(templateXMLCurrencies);
        }
    }

    private TemplateXMLCurrencies getTemplatesXMLCurrencies(String xmlText) {
        XmlMapper xmlMapper = new XmlMapper();
        TemplateXMLCurrencies templateXMLCurrencies = null;
        try {
            templateXMLCurrencies = xmlMapper.readValue(xmlText, TemplateXMLCurrencies.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return templateXMLCurrencies;
    }

    private void deleteOldCurrency(TemplateXMLCurrencies templateXMLCurrencies) {
        List<Currency> currencies = getAll();
        for (Currency currency : currencies) {
            if (templateXMLCurrencies.isLoadedById(currency.getId())) {
                deleteCurrency(currency);
            }
        }
    }

    private void createNewCurrency(TemplateXMLCurrencies templateXMLCurrencies) {
        for (TemplateCurrency tCurrency : templateXMLCurrencies.getCurrencies()) {
            Currency currency = currencyRepository.getByName(tCurrency.getName());
            if (currency == null) {
                createCurrency(getCurrencyByTemplate(tCurrency));
            }
            else {
                updateCurrency(currency, tCurrency);
            }
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
        logger.info(getMarker("services"), String.format("getActualCurrency(%s) = %f", nameCurrency, value));
        return value;
    }

    private HttpGet getHttpResponse(String nameCurrency) {
        return new HttpGet(String.format(ADDRESS_API, nameCurrency));
    }
}