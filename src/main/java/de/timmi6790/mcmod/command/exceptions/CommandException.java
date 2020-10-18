package de.timmi6790.mcmod.command.exceptions;


import de.timmi6790.mcmod.utilities.MessageBuilder;

public class CommandException extends RuntimeException {
    private final MessageBuilder message;

    public CommandException(final MessageBuilder message) {
        super("");

        this.message = message;
    }

    public MessageBuilder getMessageBuilder() {
        return this.message;
    }
}
