package com.github.av.bytelegrambot.job;

import com.github.av.bytelegrambot.service.DatabaseUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DatabaseUpdateJob {

    private final DatabaseUpdateService databaseUpdateService;

    @Autowired
    public DatabaseUpdateJob(DatabaseUpdateService databaseUpdateService) {
        this.databaseUpdateService = databaseUpdateService;
    }


    // every 24 hours
    @Scheduled(fixedRateString = "${bot.databaseUpdateFixedRate}")
    public void updateDatabase(){
        databaseUpdateService.updateDB();
    }


}
