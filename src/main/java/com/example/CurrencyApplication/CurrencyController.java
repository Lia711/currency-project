package com.example.CurrencyApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;

    @ExceptionHandler
    public ResponseEntity<String> handleExceptions(ServerErrorException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @GetMapping("/currencies") //list of currency names and codes
    public ResponseEntity<Map<String, String>> getCurrencies() throws ServerErrorException {
        Map<String, String> result = currencyService.getCurrencies();
        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(currencyService.getCurrencies());
        } else throw new ServerErrorException();
    }

    @GetMapping("/latest/{base}") //latest rates for specific currency
    public ResponseEntity<Map<String, Float>> getLatest(@PathVariable String base) {
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.getLatest(base));
    }

    @GetMapping("/json") //manually create file storing current rates
    public String getjson() throws IOException {
        currencyService.createJSON();
        return "created";
    }

    @GetMapping("/readjson/{base}") //get stored rates (auto-refreshes every day at 10am)
    public Map<String, Float> readJson(@PathVariable String base) throws IOException {
        return currencyService.getJsonData(base);
    }

    @GetMapping("/compare/{base}") //compare current rates to ones in stored data
    public Map<String, Float> compare(@PathVariable String base) throws IOException {
        return currencyService.compareLatestToSaved(base);
    }
}
