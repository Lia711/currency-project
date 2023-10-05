package com.example.CurrencyApplication;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Map;

@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String name;
    private Map<String, Map<String, String>> rates;

    public Currency(long id, String code, String name, Map<String, Map<String, String>> rates) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.rates = rates;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    @JsonProperty("base_currency_code")
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    @JsonProperty("base_currency_name")
    public void setName(String name) {
        this.name = name;
    }
    public Map<String, Map<String, String>> getRates() {
        return rates;
    }
    @JsonProperty("rates")
    public void setRates(Map<String, Map<String, String>> rates) {
        this.rates = rates;
    }
}