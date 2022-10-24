package com.github.av.bytelegrambot.service;


import com.github.av.bytelegrambot.bot.AvbyTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class BotMessageServiceImpl implements BotMessageService{

    private final AvbyTelegramBot bot;

    @Autowired
    public BotMessageServiceImpl(AvbyTelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.setText(message);

        try{
            bot.execute(sm);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
