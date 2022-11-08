package com.github.av.bytelegrambot.bot;

import com.github.av.bytelegrambot.AvbyApiClientImpl;
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
    AvbyApiClientImpl avbyApiClient;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        if(avbyApiClient.getCarLibraryService().getBrandService().getById(1).isEmpty()) {
            avbyApiClient.initDB();
        }
        try {
            telegramBotsApi.registerBot(bot);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
