package com.github.av.bytelegrambot.bot;

import com.github.av.bytelegrambot.AvbyApiClient;
import com.github.av.bytelegrambot.AvbyApiClientImpl;
import com.github.av.bytelegrambot.command.CommandContainer;
import com.github.av.bytelegrambot.command.NotACommand;
import com.github.av.bytelegrambot.config.BotConfig;
import com.github.av.bytelegrambot.repository.entity.BrandEntity;
import com.github.av.bytelegrambot.repository.entity.ModelEntity;
import com.github.av.bytelegrambot.service.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.NoSuchElementException;

import static com.github.av.bytelegrambot.command.CommandName.NOT_A_COMMAND;
import static com.github.av.bytelegrambot.command.CommandName.SEARCH;
import static com.github.av.bytelegrambot.command.CommandName.BRANDS;


@Component
public class AvbyTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";

    @Getter
    @Setter
    private String carInfo;

    private final BotConfig botConfig;

    private final AvbyApiClientImpl avbyApiClient;

    private final CommandContainer commandContainer;

    private final LocalizationServiceImpl localizationService;

    private final CarLibraryService carLibraryService;


    @Autowired
    public AvbyTelegramBot(BotConfig botConfig, AvbyApiClientImpl avbyApiClient, LocalizationServiceImpl localizationService, CarLibraryService carLibraryService){
        this.botConfig = botConfig;
        this.localizationService = localizationService;
        this.avbyApiClient = avbyApiClient;
        this.carLibraryService = carLibraryService;
        this.commandContainer = new CommandContainer(new BotMessageServiceImpl(this), localizationService, avbyApiClient);

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
                if (commandIdentifier.equals(SEARCH.getCommandName())){
                    avbyApiClient.setState("search");
                    commandContainer.retrieveCommand(BRANDS.getCommandName()).execute(update);
                }
            }
            else{
                if (avbyApiClient.getState().equals("")) {
                    commandContainer.retrieveNotACommand().execute(update, avbyApiClient.getState());
                }else if(avbyApiClient.getState().equals("search")){
                    try {
                        commandContainer.retrieveNotACommand().execute(update, avbyApiClient.getState());
                        avbyApiClient.setState("brand");
                    }catch (NoSuchElementException | NumberFormatException e){
                        avbyApiClient.setState("brand-exception");
                        commandContainer.retrieveNotACommand().execute(update, avbyApiClient.getState());
                        avbyApiClient.setState("");
                    }

                }else if(avbyApiClient.getState().equals("brand")){
                    try {
                        commandContainer.retrieveNotACommand().execute(update, avbyApiClient.getState());
                        avbyApiClient.setState("model");
                    }catch (NoSuchElementException | NumberFormatException e){
                        avbyApiClient.setState("model-exception");
                        commandContainer.retrieveNotACommand().execute(update, avbyApiClient.getState());
                        avbyApiClient.setState("");
                    }
                }else if(avbyApiClient.getState().equals("model")){
                    try {
                        commandContainer.retrieveNotACommand().execute(update, avbyApiClient.getState());
                    }catch (NoSuchElementException | NumberFormatException e){
                        avbyApiClient.setState("generation-exception");
                        commandContainer.retrieveNotACommand().execute(update, avbyApiClient.getState());
                    }
                    avbyApiClient.setState("");
                }
            }

        }
    }
}
