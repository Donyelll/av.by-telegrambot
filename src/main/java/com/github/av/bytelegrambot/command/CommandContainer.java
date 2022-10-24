package com.github.av.bytelegrambot.command;

import com.github.av.bytelegrambot.service.BotMessageService;
import com.github.av.bytelegrambot.service.LocalizationService;
import com.github.av.bytelegrambot.service.LocalizationServiceImpl;
import com.google.common.collect.ImmutableMap;

import static com.github.av.bytelegrambot.command.CommandName.*;

public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(BotMessageService botMessageService, LocalizationServiceImpl localizationService) {

        commandMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(botMessageService,localizationService))
                .put(STOP.getCommandName(), new StopCommand(botMessageService))
                .put(LANG.getCommandName(), new LangCommand(botMessageService,localizationService))
                .build();
        unknownCommand = new UnknownCommand(botMessageService);
    }

    public Command retrieveCommand(String commandIdentifier){
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
