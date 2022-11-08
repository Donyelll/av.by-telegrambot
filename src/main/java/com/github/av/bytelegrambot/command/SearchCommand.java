package com.github.av.bytelegrambot.command;

import com.github.av.bytelegrambot.service.BotMessageService;
import com.github.av.bytelegrambot.service.LocalizationService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SearchCommand implements Command{

    private final BotMessageService botMessageService;

    private final LocalizationService localizationService;

    private static final String SEARCH_MESSAGE_KEY = "search_message";

    public SearchCommand(BotMessageService botMessageService, LocalizationService localizationService) {
        this.botMessageService = botMessageService;
        this.localizationService = localizationService;
    }

    @Override
    public void execute(Update update) {
        botMessageService.sendMessage(update.getMessage().getChatId().toString(), localizationService.getMessage(SEARCH_MESSAGE_KEY));

    }
}
