package com.github.sysimp.services;

import com.github.sysimp.entities.Currency;
import com.github.sysimp.entities.Rate;
import com.github.sysimp.repositories.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {


    private CurrencyService currencyService;
    private List<Currency> currencies;

    @BeforeEach
    void setUp() {

        currencies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Currency currency = mock(Currency.class);
            when(currency.getId()).thenReturn(Long.valueOf(i));
            when(currency.getValue()).thenReturn(i + 10d);
            if (i % 2 == 0) {
                when(currency.getName()).thenReturn("USD");
            }
            else {
                when(currency.getName()).thenReturn("RUB");
            }
            currencies.add(currency);
        }

        CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
        when(currencyRepository.findAll()).thenReturn(currencies);

        currencyService = new CurrencyService(currencyRepository);
    }

    @Test
    void getAll() {
        assertArrayEquals(currencyService.getAll().toArray(), currencies.toArray());
    }

    @Test
    void getAllowCurrencies() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                list.add("USD");
            }
            else {
                list.add("RUB");
            }
        }
        assertArrayEquals(currencyService.getAllowCurrencies().toArray(), list.toArray());
    }

    @Test
    void createRequestForRate() {
        assertEquals("USD_EUR", currencyService.createRequestForRate("USD", "EUR"));
        assertEquals("RUB_RUB", currencyService.createRequestForRate("RUB", "RUB"));
    }

    @Test
    void createRequestForRateCurrency() {
        Currency currencyFrom = mock(Currency.class);
        when(currencyFrom.getName()).thenReturn("USD");

        Currency currencyTo = mock(Currency.class);
        when(currencyTo.getName()).thenReturn("RUB");

        assertEquals("USD_RUB", currencyService.createRequestForRate(currencyFrom, currencyTo));
    }

    @Test
    void checkRequest() {
        String request1 = "EUR_BTC";
        String request2 = "EUR_EUR";
        assertTrue(currencyService.checkRequest(request1));
        assertTrue(currencyService.checkRequest(request2));

        String request3 = "_EUR_BTC_USD";
        String request4 = "_EUR_USD_";
        String request5 = "_EUR_UD_";
        String request6 = "EUR_UD";
        String request7 = "EUR_USSD";
        String request8 = "EUR_U_D";
        assertFalse(currencyService.checkRequest(request3));
        assertFalse(currencyService.checkRequest(request4));
        assertFalse(currencyService.checkRequest(request5));
        assertFalse(currencyService.checkRequest(request6));
        assertFalse(currencyService.checkRequest(request7));
        assertFalse(currencyService.checkRequest(request8));
    }

    @Test
    void getRateRequestBad() {
        String request = "UD_EUR";
        assertNull(currencyService.getRate(request));
    }

    @Test
    void getRateRequestSelf() {
        String request = "USD_USD";
        Rate rate = currencyService.getRate(request);

        assertEquals("USD", rate.getFromCurrency());
        assertEquals("USD", rate.getToCurrency());
        assertEquals(1, rate.getValue());
    }

    @Test
    void getRateAllowedButCurrencyIsNull() {
        String request = "RUB_EUR";

        CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
        when(currencyRepository.getByName("RUB")).thenReturn(null);
        when(currencyRepository.getByName("EUR")).thenReturn(null);

        CurrencyService currencyService = new CurrencyService(currencyRepository);
        Rate rate = currencyService.getRate(request);

        assertNull(rate);
    }

    @Test
    void getRateAllowed() {
        String request = "RUB_EUR";
        Currency currencyFrom = mock(Currency.class);
        when(currencyFrom.getId()).thenReturn(1L);
        when(currencyFrom.getName()).thenReturn("RUB");
        when(currencyFrom.getValue()).thenReturn(0.015d);

        Currency currencyTo = mock(Currency.class);
        when(currencyTo.getId()).thenReturn(2L);
        when(currencyTo.getName()).thenReturn("EUR");
        when(currencyTo.getValue()).thenReturn(1.14);

        CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
        when(currencyRepository.getByName("RUB")).thenReturn(currencyFrom);
        when(currencyRepository.getByName("EUR")).thenReturn(currencyTo);

        CurrencyService currencyService = spy(new CurrencyService(currencyRepository));
        Rate rate = currencyService.getRate(request);

        verify(currencyService).checkRequest("RUB_EUR");

        assertEquals(0.013157894736842106, rate.getValue());
        assertEquals("RUB", rate.getFromCurrency());
        assertEquals("EUR", rate.getToCurrency());
    }

    @Test
    void updateServiceFromXmlAllDeleteAndCreate() throws IOException {
        CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
        CurrencyService currencyService = spy(new CurrencyService(currencyRepository));

        List<Currency> list = new ArrayList<>();
        list.add(new Currency(1, "USD", "Доллар США", 1, null));
        list.add(new Currency(2, "RUB", "Российский рубль", 1, null));
        list.add(new Currency(3, "EUR", "Европейская валюта", 1, null));
        list.add(new Currency(4, "SOS", "Сомалийский шиллинг", 1, null));
        list.add(new Currency(5, "AZN", "Азербайджанский манат", 1, null));

        when(currencyRepository.findAll()).thenReturn(list).thenReturn(new ArrayList<>());

        InputStream inputStreamFile = new ClassPathResource("currencyCreateTest.xml").getInputStream();
        currencyService.updateAllCurrencyFromXml(inputStreamFile);

        for (Currency currency : list) {
            verify(currencyService).createCurrency(currency);
        }
        verify(currencyService, times(list.size())).deleteCurrency(any(Currency.class));
        verify(currencyService, times(list.size())).createCurrency(any(Currency.class));
    }

    @Test
    void updateServiceFromXmlAllUpdate() throws IOException {
        CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
        CurrencyService currencyService = spy(new CurrencyService(currencyRepository));

        Currency currency = mock(Currency.class);
        when(currencyRepository.getByName(anyString())).thenReturn(currency);

        InputStream inputStreamFile = new ClassPathResource("currencyCreateTest.xml").getInputStream();
        currencyService.updateAllCurrencyFromXml(inputStreamFile);

        verify(currencyService, times(5)).updateCurrency(any(Currency.class), any(TemplateCurrency.class));
    }

    @Test
    void updateServiceFromXmlException() {
        try (InputStream inputStreamFile = new ClassPathResource("badName.xml").getInputStream()) {
            currencyService.updateAllCurrencyFromXml(inputStreamFile);
            fail("Test fail!");
        } catch (IOException exception) {
            assertNotEquals("", exception.getMessage());
        }
    }

    @Test
    void updateCurrencyFromApi() {
        CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
        CurrencyService currencyService = spy(new CurrencyService(currencyRepository));

        List<Currency> list = new ArrayList<>();
        list.add(new Currency(1, "USD", "Доллар США", -2, null));
        list.add(new Currency(2, "RUB", "Российский рубль", -2, null));
        list.add(new Currency(3, "EUR", "Европейская валюта", -2, null));
        list.add(new Currency(4, "SOS", "Сомалийский шиллинг", -2, null));
        list.add(new Currency(5, "AZN", "Азербайджанский манат", -2, null));

        when(currencyRepository.findAll()).thenReturn(list);

        currencyService.updateCurrencies();
        for (Currency currency : list) {
            if (currency.getValue() == -2) {
                fail("Update fail");
            }
            verify(currencyService).editCurrency(currency);
        }
        verify(currencyService, times(5)).editCurrency(any(Currency.class));
    }
}