package com.github.av.bytelegrambot.command;

import com.github.av.bytelegrambot.AvbyApiClientImpl;
import com.github.av.bytelegrambot.bot.AvbyTelegramBot;
import com.github.av.bytelegrambot.service.BotMessageService;
import com.github.av.bytelegrambot.service.LocalizationService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BackCommand implements Command{

    private final BotMessageService botMessageService;

    private final AvbyApiClientImpl avbyApiClient;

    private final LocalizationService localizationService;


    public BackCommand(BotMessageService botMessageService, LocalizationService localizationService, AvbyApiClientImpl avbyApiClient) {
        this.botMessageService = botMessageService;
        this.avbyApiClient = avbyApiClient;
        this.localizationService = localizationService;
    }

    @Override
    public void execute(Update update) {
        avbyApiClient.setState("");
        botMessageService.sendMessage(update.getMessage().getChatId().toString(), localizationService.getMessage("help_command_message_key") );
    }
}
