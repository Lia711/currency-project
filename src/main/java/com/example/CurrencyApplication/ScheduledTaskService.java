package com.example.CurrencyApplication;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;


@Component
public class ScheduledTaskService {
    File jsonFile = new File("C:\\Users\\liana\\nology\\currencyProject\\CurrencyApplication\\savedRates.json");
    CurrencyService currencyService;
    @Scheduled(cron = "0 0 10 * * *") //10am every day
    public void getDailyData() {
        //if json doesn't exist call creator
        //if it does, replace it

        System.out.println("Daily Rates Retrieved");
    }

//    @Scheduled(fixedDelay = 3000) //10am every day
//    public void checkFile() {
//        if (jsonFile.isFile()){
//            System.out.println("exists");
//        } else System.out.println("does not exist");
//    }//always returns true
}
