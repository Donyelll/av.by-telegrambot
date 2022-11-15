package com.github.av.bytelegrambot.command;

public enum CommandName {

    START("/start"),
    STOP("/stop"),
    SEARCH("/search"),
    NOT_A_COMMAND("foo"),
    LANG("/lang"),
    BRANDS("/brands"),
    HELP("/help"),
    LINKS("/links"),
    CHARTS("/charts"),
    BACK("/back");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
