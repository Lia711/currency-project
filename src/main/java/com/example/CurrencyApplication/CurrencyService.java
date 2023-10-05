package com.example.CurrencyApplication;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Service
public class CurrencyService {
    @Autowired
    RestTemplateClass restTemplate;
    private CurrencyList currencyList;

    FileWriter fileWriter = new FileWriter("savedRates.json");

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

    public void createJSON() throws IOException {
        Map<String, Float> responseUSD = getLatest("USD");
        JSONObject ratesUSD = new JSONObject();
        for (String rate : responseUSD.keySet()){
            ratesUSD.put(rate, responseUSD.get(rate));
        }

        Map<String, Float> responseEUR = getLatest("EUR");
        JSONObject ratesEUR = new JSONObject();
        for (String rate : responseEUR.keySet()){
            ratesEUR.put(rate, responseEUR.get(rate));
        }

        Map<String, Float> responseGBP = getLatest("GBP");
        JSONObject ratesGBP = new JSONObject();
        for (String rate : responseGBP.keySet()){
            ratesGBP.put(rate, responseGBP.get(rate));
        }

        Map<String, Float> responseJPY = getLatest("JPY");
        JSONObject ratesJPY = new JSONObject();
        for (String rate : responseJPY.keySet()){
            ratesJPY.put(rate, responseJPY.get(rate));
        }

        Map<String, Float> responseAUD = getLatest("AUD");
        JSONObject ratesAUD = new JSONObject();
        for (String rate : responseAUD.keySet()){
            ratesAUD.put(rate, responseAUD.get(rate));
        }

        JSONObject ratesAll = new JSONObject();
        ratesAll.put("USD", ratesUSD);
        ratesAll.put("EUR", ratesEUR);
        ratesAll.put("GBP", ratesGBP);
        ratesAll.put("JPY", ratesJPY);
        ratesAll.put("AUD", ratesAUD);

        try {
            fileWriter.write(ratesAll.toString());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

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

    public String convertToAny(String base, String target, String amount) {
        String url = "https://api.getgeoapi.com/v2/currency/convert\n" +
                "?api_key=67ff4bd5ca1d969ced576da7b2021b4428be88d6\n" +
                "&from=" + base +
                "&to=" + target +
                "&amoun" + amount +
                "&format=json";
        return restTemplate.getForObject(url, String.class);
    }

}
