package com.github.av.bytelegrambot.bot;

import com.github.av.bytelegrambot.command.CommandContainer;
import com.github.av.bytelegrambot.config.BotConfig;
import com.github.av.bytelegrambot.repository.entity.Brand;
import com.github.av.bytelegrambot.repository.entity.Model;
import com.github.av.bytelegrambot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class AvbyTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";

    private final BotConfig botConfig;

    private final CommandContainer commandContainer;

    private final LocalizationServiceImpl localizationService;

    private final BrandService brandService;

    private final ModelService modelService;

    private final GenerationService generationService;

    @Autowired
    public AvbyTelegramBot(BotConfig botConfig, LocalizationServiceImpl localizationService, BrandService brandService, ModelService modelService, GenerationService generationService){
        this.botConfig = botConfig;
        this.localizationService = localizationService;
        this.brandService = brandService;
        this.commandContainer = new CommandContainer(new BotMessageServiceImpl(this), localizationService);
        this.modelService = modelService;
        this.generationService = generationService;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            if (message.startsWith(COMMAND_PREFIX)){
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            }
            else{
                Brand someBrand = new Brand();
                Model someModel = new Model();
                someBrand.setName(message);
                someBrand.setId(1);
                brandService.save(someBrand);
                System.out.println("Added " + message + " brand to db");
                someModel.setName("M4");
                someModel.setId(2);
                someModel.setBrand(someBrand);
                System.out.println("Trying to save model to db");
                modelService.save(someModel);
                System.out.println("added RS4 model to db");
                System.out.println("Trying to get that brand");
                System.out.println(brandService.getByName(message).get().getName());
                System.out.println("trying to get model");
                System.out.println(modelService.getAllByBrand(1));
            }

        }
    }
}
