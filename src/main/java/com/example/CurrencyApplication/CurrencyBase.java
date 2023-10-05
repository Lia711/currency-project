package com.example.CurrencyApplication;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class CurrencyBase {
    @JsonProperty("conversion_rates")
    private Map<String, Float> conversions;

    public Map<String, Float> getConversions() {
        return conversions;
    }
}
