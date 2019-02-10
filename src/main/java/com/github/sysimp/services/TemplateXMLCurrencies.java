package com.github.sysimp.services;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "currencies")
class TemplateXMLCurrencies {

    @JacksonXmlProperty(localName = "noNamespaceSchemaLocation")
    private String noNamespaceSchemaLocation;

    @JacksonXmlProperty(localName = "currency")
    @JacksonXmlElementWrapper(useWrapping = false)
    private TemplateCurrency[] currencies;

    public TemplateCurrency[] getCurrencies() {
        return currencies;
    }

    public boolean isLoadedById(long id) {
        for (TemplateCurrency templateCurrency : currencies) {
            if (templateCurrency.getId() == id) {
                return true;
            }
        }

        return false;
    }
}

class TemplateCurrency {

    @JacksonXmlProperty(localName = "id")
    private long id;
    @JacksonXmlProperty(localName = "name")
    private String name;
    @JacksonXmlProperty(localName = "description")
    private String description;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("TemplateCurrency{id:%d; name:%s; description:%s}", id, name, description);
    }
}

