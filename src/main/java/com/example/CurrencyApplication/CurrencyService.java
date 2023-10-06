package com.example.CurrencyApplication;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public void mapOverRates(String base) {
        Map<String, Float> response = getLatest(base);
        JSONObject rates = new JSONObject();
        for (String rate : response.keySet()) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Float> compareLatestToSaved(String base) throws IOException {
        Map<String, Float> latest = getLatest(base);
        Map<String, Float> saved = getJsonData(base);
        Map<String, Float> compared = new HashMap<>();
        for (String currency : latest.keySet()) {
            if (saved.keySet().contains(currency)) {
                //show percentage increase/decrease; round to two decimal places
                compared.put(currency, (float) Math.round((((latest.get(currency) - saved.get(currency))/ latest.get(currency)))*100*100)/100);
            }
        }
        return compared;
    }

    public Map<String, Float> getJsonData(String base) throws IOException {
        Map<String, Float> result = new HashMap<>();
        String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\liana\\nology\\currencyProject\\CurrencyApplication\\savedRates.json")));
        JSONObject allData = new JSONObject(content);
        JSONObject dataForCurrency = allData.getJSONObject(base);
        for (String code : dataForCurrency.keySet()) {
            result.put(code, dataForCurrency.getFloat(code));
        }
        return result;
    }
}
