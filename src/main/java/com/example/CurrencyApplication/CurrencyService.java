package com.example.CurrencyApplication;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {
    @Autowired
    RestTemplateClass restTemplate;
    private CurrencyList currencyList;

    JSONObject ratesAll = new JSONObject();
    public CurrencyService() throws IOException {
    }

    public Map<String, String> getCurrencies() {
        String url = "https://api.getgeoapi.com/v2/currency/list\n" +
                "?api_key=67ff4bd5ca1d969ced576da7b2021b4428be88d6&format=json";
        currencyList = restTemplate.getForObject(url, CurrencyList.class);
            return currencyList.getCurrencies();
    }

    public Map<String, Float> getLatest(String base) {
        String url = "https://v6.exchangerate-api.com/v6/59e2bc00aa987fdbf3a0811d/latest/\n" +
                base;
        CurrencyBase latestBase = restTemplate.getForObject(url, CurrencyBase.class);
        return latestBase.getConversions();
    }

    public Map<String, Float> getHistorical(String base, String year, String month, String day){
        String url = "https://v6.exchangerate-api.com/v6/59e2bc00aa987fdbf3a0811d/history/\n" +
                base + year + month + day;
        CurrencyBase historicalBase = restTemplate.getForObject(url, CurrencyBase.class);
        return historicalBase.getConversions();
    }

    public void mapOverRates(String base){
        Map <String, Float> response = getLatest(base);
        JSONObject rates = new JSONObject();
        for (String rate : response.keySet()){
            rates.put(rate, response.get(rate));
        }
        ratesAll.put(base, rates);
    }
    public void createJSON() throws IOException {
        mapOverRates("USD");
        mapOverRates("EUR");
        mapOverRates("GBP");
        mapOverRates("JPY");
        mapOverRates("AUD");

        try (FileWriter fileWriter = new FileWriter("savedRates.json")) {
            fileWriter.write(ratesAll.toString());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //not available
    public Map<String, Float> compareCurrentAndHistorical(String base, String year, String month, String day){
        Map<String, Float> latest = getLatest(base);
        Map<String, Float> historical = getHistorical(base, year, month, day);
        Map<String, Float> compared = null;
        for (String currency : latest.keySet()){
            if (historical.keySet().contains(currency)){
                compared.put(currency, latest.get(currency)-historical.get(currency));
            }
        } return compared;
    }

    public Map<String, Float> compareLatestToSaved(String base) throws FileNotFoundException {
        Map<String, Float> latest = getLatest(base);
        Map<String, Float> json = getJsonData(base);
        Map<String, Float> compared = null;
        for (String currency : latest.keySet()){
            if (json.keySet().contains(currency)){
                compared.put(currency, latest.get(currency)-json.get(currency));
            }
        } return compared;
    }

    public Map<String, Float> getJsonData(String base) throws FileNotFoundException {
        Map<String, Float> result = new HashMap<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject allData = (JSONObject) parser.parse(
                    new FileReader("C:\\Users\\liana\\nology\\currencyProject\\CurrencyApplication\\savedRates.json"));
            Object dataForCurrency =  allData.get(base);
            for (String code : dataForCurrency.keySet){
                result.put(code, Float.parseFloat(dataForCurrency.get(code)));
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

}
