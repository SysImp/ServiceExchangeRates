package com.github.sysimp.controllers.validator;

import com.github.sysimp.services.CurrencyService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class ExchangeFormValidator implements Validator {

    private static final Pattern PATTERN = Pattern.compile("[0-9]+");
    private CurrencyService currencyService;

    ExchangeFormValidator(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ExchangeForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ExchangeForm exchangeForm = (ExchangeForm) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromCurrency", "NotEmpty.exchangeForm.fromCurrency");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toCurrency", "NotEmpty.exchangeForm.toCurrency");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "factor", "NotEmpty.exchangeForm.factor");

        List<String> allowCurrencies = currencyService.getAllowCurrencies();
        if (!(allowCurrencies.contains(exchangeForm.getFromCurrency()))) {
            errors.rejectValue("fromCurrency", "BadCurrency.exchangeForm.fromCurrency");
        }
        if (!(allowCurrencies.contains(exchangeForm.getToCurrency()))) {
            errors.rejectValue("toCurrency", "BadCurrency.exchangeForm.toCurrency");
        }
        if (!PATTERN.matcher(exchangeForm.getFactor()).matches()) {
            errors.rejectValue("factor", "NotNumber.exchangeForm.factor");
        }
    }
}
