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

    @GetMapping("/currencies")
    public ResponseEntity<Map<String, String>> getCurrencies() throws ServerErrorException{
        Map<String, String> result = currencyService.getCurrencies();
        if (result!=null){
            return ResponseEntity.status(HttpStatus.OK).body(currencyService.getCurrencies());
        }
        else throw new ServerErrorException();
    }

    @GetMapping("/latest/{base}")
    public ResponseEntity<Map<String, Float>> getLatest(@PathVariable String base) {
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.getLatest(base));
    }

    @GetMapping("/json")
    public String getjson() throws IOException {
        currencyService.createJSON();
        return "created";
    }

    @GetMapping("/historical/{base}/{year}/{month}/{day}")
    public ResponseEntity<Map<String, Float>> getHistorical(@PathVariable String base,
                                                @PathVariable String year,
                                                @PathVariable String month,
                                                @PathVariable String day) {
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.getHistorical(base, year, month, day));
    }

    @GetMapping("/convert/{base}/{target}/{amount}")
    public ResponseEntity<String> convertToAny(@PathVariable("base") String base,
                                               @PathVariable("target") String target,
                                               @PathVariable("amount") String amount) {
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.convertToAny(base, target, amount));
    }
}
