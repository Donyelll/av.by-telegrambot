package com.github.av.bytelegrambot.service;


import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalizationServiceImpl implements LocalizationService{

    private Locale currentLocale;

    private ResourceBundleMessageSource messageSource;

    public ResourceBundleMessageSource getMessageSource() {
        return messageSource;
    }

    public LocalizationServiceImpl() {
        this.messageSource = new ResourceBundleMessageSource();
        this.messageSource.setBasename("lang/res");
        this.messageSource.setDefaultEncoding("UTF-8");
        this.setCurrentLocale(new Locale("ru", "RU"));
        this.messageSource.setDefaultLocale(getCurrentLocale());
    }

    @Override
    public String getMessage(String messageKey) {
        return messageSource.getMessage(messageKey,null,getCurrentLocale());
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }
}
