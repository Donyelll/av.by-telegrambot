package com.github.av.bytelegrambot.command;

import com.github.av.bytelegrambot.AvbyApiClientImpl;
import com.github.av.bytelegrambot.repository.entity.BrandEntity;
import com.github.av.bytelegrambot.repository.entity.ModelEntity;
import com.github.av.bytelegrambot.service.BotMessageService;
import com.github.av.bytelegrambot.service.CarLibraryService;
import com.github.av.bytelegrambot.service.LocalizationService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrandsCommand implements Command{

    private final BotMessageService botMessageService;
    private final AvbyApiClientImpl avbyApiClient;


    public BrandsCommand(BotMessageService botMessageService, AvbyApiClientImpl avbyApiClient) {

        this.botMessageService = botMessageService;
        this.avbyApiClient = avbyApiClient;
    }

    @Override
    public void execute(Update update) {
        StringBuilder builder = new StringBuilder();
        int i = 1;
        List<BrandEntity> brands = avbyApiClient.getCarLibraryService().getBrandService().getAllBrands();
        for (BrandEntity model : brands) {
            builder.append(i++).append(". ").append(model.getName()).append("\n");
        }
        botMessageService.sendMessage(update.getMessage().getChatId().toString(),builder.toString());
    }
}
