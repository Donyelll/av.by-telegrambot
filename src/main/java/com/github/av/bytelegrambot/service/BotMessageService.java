package com.github.av.bytelegrambot.service;

import java.util.List;

public interface BotMessageService {

    void sendMessage(String chatId, String message);

    void sendPhotos(String chatId, List<String> photoURLs);
}