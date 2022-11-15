package com.github.av.bytelegrambot.service;


import com.github.av.bytelegrambot.bot.AvbyTelegramBot;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Service
public class BotMessageServiceImpl implements BotMessageService {

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
        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhotos(String chatId, List<String> photoURLs) {
        SendMediaGroup smg = new SendMediaGroup();
        smg.setChatId(chatId);
        List<InputMedia> inputMediaPhotoList = new ArrayList<>();

        for (String url : photoURLs) {
            InputMediaPhoto inp = new InputMediaPhoto();
            inp.setMedia(url);
            inputMediaPhotoList.add(inp);

        }

            smg.setMedias(inputMediaPhotoList);


            try {
                bot.execute(smg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

    }
}
