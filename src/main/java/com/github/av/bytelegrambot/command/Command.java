package com.github.av.bytelegrambot.command;


import org.telegram.telegrambots.meta.api.objects.Update;


public interface Command {

    void execute(Update update);

}
