package com.github.av.bytelegrambot.bot;

import com.github.av.bytelegrambot.AvbyApiClientImpl;
import com.github.av.bytelegrambot.service.DatabaseUpdateService;
import com.github.av.bytelegrambot.service.DatabaseUpdateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {

    @Autowired
    AvbyTelegramBot bot;

    @Autowired
    DatabaseUpdateService databaseUpdateService;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(bot);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
