package com.github.av.bytelegrambot.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.username}")
    private  String username;

    @Value("${bot.token}")
    private  String token;
}
