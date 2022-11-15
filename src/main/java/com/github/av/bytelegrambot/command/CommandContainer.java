package com.github.av.bytelegrambot.command;

import com.github.av.bytelegrambot.AvbyApiClient;
import com.github.av.bytelegrambot.AvbyApiClientImpl;
import com.github.av.bytelegrambot.service.BotMessageService;
import com.github.av.bytelegrambot.service.LocalizationServiceImpl;
import com.google.common.collect.ImmutableMap;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.av.bytelegrambot.command.CommandName.*;

public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;
    private final NotACommand notACommand;

    public CommandContainer(BotMessageService botMessageService, LocalizationServiceImpl localizationService, AvbyApiClientImpl avbyApiClient) {

        commandMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(botMessageService,localizationService))
                .put(STOP.getCommandName(), new StopCommand(botMessageService))
                .put(LANG.getCommandName(), new LangCommand(botMessageService,localizationService))
                .put(BRANDS.getCommandName(), new BrandsCommand(botMessageService, avbyApiClient))
                .put(SEARCH.getCommandName(), new SearchCommand(botMessageService, localizationService))
                .put(BACK.getCommandName(), new BackCommand(botMessageService, localizationService, avbyApiClient))
                .put(LINKS.getCommandName(), new LinksCommand(botMessageService, localizationService, avbyApiClient))
                .put(CHARTS.getCommandName(), new ChartsCommand(botMessageService, localizationService, avbyApiClient))
                .put(HELP.getCommandName(), new HelpCommand(botMessageService,localizationService))
                .build();
        unknownCommand = new UnknownCommand(botMessageService, localizationService);
        notACommand = new NotACommand(botMessageService,localizationService, avbyApiClient);
    }

    public Command retrieveCommand(String commandIdentifier){
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

    public NotACommand retrieveNotACommand(){
        return notACommand;
    }
}
