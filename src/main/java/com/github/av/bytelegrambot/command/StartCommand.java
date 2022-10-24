package com.github.av.bytelegrambot.command;

import com.github.av.bytelegrambot.service.BotMessageService;
import com.github.av.bytelegrambot.service.BotMessageServiceImpl;
import com.github.av.bytelegrambot.service.LocalizationService;
import com.github.av.bytelegrambot.service.LocalizationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements Command{

    private final BotMessageService botMessageService;

    private final LocalizationService localizationService;

    private static final String START_MESSAGE_KEY = "start_message";

    public StartCommand(BotMessageService botMessageService, LocalizationService localizationService) {
        this.botMessageService = botMessageService;
        this.localizationService = localizationService;
    }

    @Override
    public void execute(Update update) {
        botMessageService.sendMessage(update.getMessage().getChatId().toString(), localizationService.getMessage(START_MESSAGE_KEY));
    }
}
