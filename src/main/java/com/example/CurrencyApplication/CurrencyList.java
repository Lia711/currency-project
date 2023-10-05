package com.example.CurrencyApplication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.util.List;
import java.util.Map;

public class CurrencyList {
    @JsonProperty("currencies")
    private Map<String, String> currencies;

    public Map<String, String> getCurrencies() {
        return currencies;
    }
}
