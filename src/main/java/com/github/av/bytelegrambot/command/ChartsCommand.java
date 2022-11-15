package com.github.av.bytelegrambot.command;

import com.github.av.bytelegrambot.AvbyApiClientImpl;
import com.github.av.bytelegrambot.bot.AvbyTelegramBot;
import com.github.av.bytelegrambot.service.BotMessageService;
import com.github.av.bytelegrambot.service.BotMessageServiceImpl;
import com.github.av.bytelegrambot.service.LocalizationService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChartsCommand implements Command{

    private final BotMessageService botMessageService;

    private final AvbyApiClientImpl avbyApiClient;

    private final LocalizationService localizationService;


    public ChartsCommand(BotMessageService botMessageService, LocalizationService localizationService, AvbyApiClientImpl avbyApiClient) {
        this.botMessageService = botMessageService;
        this.avbyApiClient = avbyApiClient;
        this.localizationService = localizationService;
    }

    @Override
    public void execute(Update update) {
        avbyApiClient.getAllAdsCarProperties();
        botMessageService.sendMessage(update.getMessage().getChatId().toString(), avbyApiClient.getCarChartsMessage());
        botMessageService.sendPhotos(update.getMessage().getChatId().toString(), avbyApiClient.getChartPhotoByURL());

    }
}
