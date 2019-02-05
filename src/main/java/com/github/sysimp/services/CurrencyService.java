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
import java.util.List;

import static org.apache.logging.log4j.MarkerManager.getMarker;

@Service
@EnableScheduling
public class CurrencyService {

    private static final Logger LOG = LogManager.getLogger(CurrencyService.class);

    //%s - name currency
    private static final String ADDRESS_API = "https://free.currencyconverterapi.com/api/v6/convert?q=%s_USD";
    private static final String PATH_XML_TEMPLATE = "static/xml/currencyTemplate.xml";

    private CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        LOG.info(getMarker("services"), "Constructor: CurrencyService(CurrencyRepository currencyRepository)");
        this.currencyRepository = currencyRepository;
    }

    public void createCurrency(Currency currency) {
        LOG.info(getMarker("services"), String.format("createCurrency: %s", currency));
        currencyRepository.save(currency);
    }

    public void editCurrency(Currency currency) {
        LOG.info(getMarker("services"), String.format("editCurrency: %s", currency));
        currencyRepository.save(currency);
    }

    public void updateCurrency(Currency currency, TemplateCurrency template) {
        LOG.info(getMarker("services"), String.format("updateCurrency: %s, template:%s", currency, template));
        currency.setId(template.getId());
        currency.setDescription(template.getDescription());

        currencyRepository.saveAndFlush(currency);
    }

    public Currency getCurrencyByTemplate(TemplateCurrency template) {
        return new Currency(template.getId(), template.getName(), template.getDescription(), -1, LocalDateTime.now());
    }

    public void deleteCurrency(Currency currency) {
        LOG.info(getMarker("services"), String.format("deleteCurrency: %s", currency));
        currencyRepository.delete(currency);
    }

    public List<Currency> getAll() {
        LOG.info(getMarker("services"), "getAll()");
        return currencyRepository.findAll();
    }

    public Currency getCurrencyByName(String name) {
        return currencyRepository.getByName(name);
    }

    public List<Currency> getAll(Sort sort) {
        LOG.info(getMarker("services"), String.format("getAll(Sort sort = %s)", sort.toString()));
        return currencyRepository.findAll(sort);
    }

    public List<String> getAllowCurrencies() {
        LOG.info(getMarker("services"), "getAllowCurrencies()");
        List<String> list = new ArrayList<>();
        for (Currency currency : getAll()) {
            list.add(currency.getName());
        }
        LOG.info(getMarker("services"), String.format("getAllowCurrencies().result = %s", list.toString()));

        return list;
    }

    public List<Rate> getListCombsRate() {
        LOG.info(getMarker("services"), "getListCombsRate()");
        List<Currency> listAllCurrencies = getAll();
        List<Rate> list = new ArrayList<>();
        //Combs
        for (int i = 0; i < listAllCurrencies.size(); i++) {
            for (int j = 0; j < listAllCurrencies.size(); j++) {
                list.add(getRate(createRequestForRate(listAllCurrencies.get(i), listAllCurrencies.get(j))));
            }
        }
        LOG.info(getMarker("services"), String.format("getListCombsRate().size = %d", list.size()));

        return list;
    }

    @Scheduled(fixedRate = 1800000)
    public void updateService() {
        LOG.info(getMarker("services"), "updateService() fixedRate = 1800000");
        updateCurrencies();
    }

    public String createRequestForRate(Currency currencyFrom, Currency currencyTo) {
        return createRequestForRate(currencyFrom.getName(), currencyTo.getName());
    }

    public String createRequestForRate(String from, String to) {
        LOG.info(getMarker("services"), String.format("createRequestForRate(String from = %s, String to = %s)", from, to));
        return String.format("%s_%s", from, to);
    }

    public boolean checkRequest(String request) {
        LOG.info(getMarker("services"), String.format("checkRequest(%s)", request));
        if (request.length() != 7) {
            LOG.info(getMarker("services"), String.format("checkRequest(%s) return false;", request));
            return false;
        }
        //args[0] - from, args[1] - to
        String[] args = request.split("_");

        if (args.length != 2) {
            LOG.info(getMarker("services"), String.format("checkRequest(%s) return false;", request));
            return false;
        }

        LOG.info(getMarker("services"), String.format("checkRequest(%s) return true;", request));
        return true;
    }

    public Rate getRate(String request) {
        LOG.info(getMarker("services"), String.format("getRate(%s)", request));
        if (!checkRequest(request)) {
            return null;
        }

        String[] args = request.split("_");

        Rate rate;

        //args[0] == args[1]: ex: USD_USD and etc...
        if (args[0].equalsIgnoreCase(args[1])) {
            rate = new Rate(1, args[0], args[1], LocalDateTime.now());
        } else {
            rate = getRate(args[0], args[1]);
        }

        LOG.info(getMarker("services"), String.format("getRate(%s) return rate = ", rate));
        return rate;
    }

    public void updateAllCurrencyFromXml(InputStream inputStreamFile) {
        LOG.info(getMarker("services"), "START: updateAllCurrencyFromXml");
        try {
            String xmlText = IOUtils.toString(inputStreamFile, "UTF-8");
            TemplateXMLCurrencies templateXMLCurrencies = getTemplatesXMLCurrencies(xmlText);
            updateAllCurrencyFromXML(templateXMLCurrencies);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        LOG.info(getMarker("services"), "FINISH: updateAllCurrencyFromXml");
    }

    public void updateAllCurrencyFromXml() {
        try (InputStream inputStreamFile = new ClassPathResource(PATH_XML_TEMPLATE).getInputStream()) {
            updateAllCurrencyFromXml(inputStreamFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void updateCurrencies() {
        LOG.info(getMarker("services"), "START: updateCurrencies");
        for (Currency currency : getAll()) {
            double newValue = getActualCurrency(currency.getName());
            if (newValue != -1) {
                currency.setValue(newValue);
            }
            editCurrency(currency);
        }
        LOG.info(getMarker("services"), "FINISH: updateCurrencies");
    }

    private Rate getRate(String from, String to) {
        LOG.info(getMarker("services"), String.format(" private Rate getRate(from = %s, to = %s)", from, to));
        Currency currencyFrom = currencyRepository.getByName(from);
        Currency currencyTo = currencyRepository.getByName(to);

        if (currencyFrom == null || currencyTo == null) {
            LOG.info(getMarker("services"), String.format("private Rate getRate(from = %s, to = %s) return null;", from, to));
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
        LOG.info(getMarker("services"), "getActualCurrency(%s), nameCurrency");
        try (CloseableHttpClient client = HttpClients.createDefault(); CloseableHttpResponse response = client.execute(getHttpResponse(nameCurrency))) {

            InputStream inputStream = response.getEntity().getContent();
            String jText = IOUtils.toString(inputStream, "UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jNode = objectMapper.readTree(jText);

            jNode = jNode.findPath(String.format("%s_USD", nameCurrency));
            Rate rate = objectMapper.readValue(jNode.toString(), Rate.class);
            value = rate.getValue();
        } catch (Exception e) {
            LOG.error(getMarker("services"), String.format("External API is fail. \n %s", e.getStackTrace()));
        }
        LOG.info(getMarker("services"), String.format("getActualCurrency(%s) = %f", nameCurrency, value));
        return value;
    }

    private HttpGet getHttpResponse(String nameCurrency) {
        return new HttpGet(String.format(ADDRESS_API, nameCurrency));
    }
}