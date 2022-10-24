package com.github.av.bytelegrambot.bot;

import com.github.av.bytelegrambot.command.CommandContainer;
import com.github.av.bytelegrambot.config.BotConfig;
import com.github.av.bytelegrambot.service.BotMessageServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class AvbyTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";

    private final BotConfig botConfig;

    private final CommandContainer commandContainer;

    public AvbyTelegramBot(BotConfig botConfig){
        this.botConfig=botConfig;
        this.commandContainer = new CommandContainer(new BotMessageServiceImpl(this));
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
                System.out.println("????");
            }

        }
    }
}
