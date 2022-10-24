package com.github.av.bytelegrambot.command;

public enum CommandName {

    START("/start"),
    STOP("/stop"),
    LANG("/lang");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
