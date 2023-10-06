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
    CurrencyService currencyService;

    @Scheduled(cron = "0 0 10 * * *") // 10am every day
    public void getDailyData() throws IOException {
        currencyService.createJSON();
        System.out.println("Daily Rates Retrieved");
    }
}
