package com.github.av.bytelegrambot.command;

import com.github.av.bytelegrambot.service.BotMessageService;
import com.github.av.bytelegrambot.service.LocalizationService;
import com.github.av.bytelegrambot.service.LocalizationServiceImpl;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

public class LangCommand implements Command{

    private final BotMessageService botMessageService;

    private final LocalizationServiceImpl localizationService;

    private static final String LANG_MESSAGE_KEY = "lang_message";

    public LangCommand(BotMessageService botMessageService, LocalizationServiceImpl localizationService) {
        this.botMessageService = botMessageService;
        this.localizationService = localizationService;
    }

    @Override
    public void execute(Update update) {
        botMessageService.sendMessage(update.getMessage().getChatId().toString(), localizationService.getMessage(LANG_MESSAGE_KEY));
        if (localizationService.getCurrentLocale().equals(Locale.ENGLISH)){
            localizationService.setCurrentLocale(new Locale("ru", "RU"));
        }else{
            localizationService.setCurrentLocale(Locale.ENGLISH);
        }

    }
}
