package com.github.av.bytelegrambot.command;

import com.github.av.bytelegrambot.service.BotMessageService;
import com.github.av.bytelegrambot.service.BotMessageServiceImpl;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownCommand implements Command{

    private final BotMessageService botMessageService;

    String unknownMessage = "unknown message example";

    public UnknownCommand(BotMessageService botMessageService) {
        this.botMessageService = botMessageService;
    }

    @Override
    public void execute(Update update) {
        botMessageService.sendMessage(update.getMessage().getChatId().toString(), unknownMessage);
    }
}
