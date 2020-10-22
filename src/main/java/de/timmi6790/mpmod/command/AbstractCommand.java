package de.timmi6790.mpmod.command;

import de.timmi6790.mpmod.command.exceptions.CommandException;
import de.timmi6790.mpmod.utilities.DataUtilities;
import de.timmi6790.mpmod.utilities.MessageBuilder;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractCommand extends CommandBase {
    private final String name;
    private final List<String> aliases;
    private String prefix;
    private int minArgs = 0;

    private List<String> syntax = new ArrayList<>();

    public AbstractCommand(final String name) {
        this(name, null, null);
    }

    public AbstractCommand(final String name, final List<String> aliases) {
        this(name, null, aliases);
    }

    public AbstractCommand(final String name, final String prefix) {
        this(name, prefix, null);
    }

    public AbstractCommand(final String name, final String prefix, final List<String> aliases) {
        this.name = name;
        this.prefix = prefix;

        if (aliases == null) {
            this.aliases = new ArrayList<>();
        } else {
            this.aliases = aliases;
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return this.name;
    }

    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "/" + this.name;
    }

    @Override
    public List<String> getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {
        if (this.minArgs > args.length) {
            this.returnTellMissingArgs();
        }

        try {
            this.onCommand(sender, args);
        } catch (final CommandException ex) {
            if (ex.getMessageBuilder() != null) {
                this.tell(ex.getMessageBuilder());
            }
        }
    }

    public abstract void onCommand(final ICommandSender sender, final String[] args);

    protected void returnTell(final MessageBuilder messages) {
        throw new CommandException(messages);
    }

    protected void returnTellNotValidCommand(final String commandName) {
        this.returnTell(new MessageBuilder(commandName, EnumChatFormatting.YELLOW).addMessage(" is not a valid command.", EnumChatFormatting.GRAY));
    }

    protected void returnTellMissingArgs() {
        new MessageBuilder(StringUtils.capitalize(this.getCommandName()) + "-Command\n", EnumChatFormatting.GOLD)
                .addMessage("\n" + "/" + this.getCommandName() + " " + this.getCommandName() + " " + String.join(" ", this.getSyntax()), EnumChatFormatting.YELLOW).sendToPlayerWithBox();
        this.returnTell(null);
    }

    protected final void checkNotNull(final Object value, final MessageBuilder messageIfNull) {
        if (value == null) {
            this.returnTell(messageIfNull);
        }
    }

    private MessageBuilder getCheckValueErrorMessage(final Object value, final String checkType) {
        return new MessageBuilder(String.valueOf(value), EnumChatFormatting.YELLOW)
                .addMessage(" is not a valid ", EnumChatFormatting.GRAY)
                .addMessage(checkType)
                .addMessage(" value.", EnumChatFormatting.GRAY);
    }

    protected int checkInt(final Object value) {
        return this.checkInt(value, this.getCheckValueErrorMessage(value, "int"));
    }

    protected int checkInt(final Object value, final MessageBuilder falseMessage) {
        if (!DataUtilities.isInt(value)) {
            this.returnTell(falseMessage);
        }

        return Integer.parseInt(String.valueOf(value));
    }

    protected double checkDouble(final Object value) {
        return this.checkDouble(value, this.getCheckValueErrorMessage(value, "double"));
    }

    protected double checkDouble(final Object value, final MessageBuilder falseMessage) {
        if (!DataUtilities.isDouble(value)) {
            this.returnTell(falseMessage);
        }

        return Double.parseDouble(String.valueOf(value));
    }

    protected float checkFloat(final Object value) {
        return this.checkFloat(value, this.getCheckValueErrorMessage(value, "float"));
    }

    protected float checkFloat(final Object value, final MessageBuilder falseMessage) {
        if (!DataUtilities.isFloat(value)) {
            this.returnTell(falseMessage);
        }

        return Float.parseFloat(String.valueOf(value));
    }

    public boolean hasEmptyArg(final String[] args, final int position) {
        return DataUtilities.hasEmptyArg(args, position);
    }

    protected void tell(final String message) {
        this.tell(new MessageBuilder(message));
    }

    protected void tell(final MessageBuilder message) {
        new MessageBuilder("")
                .addMessage(this.prefix != null ? this.prefix + "> " : "", EnumChatFormatting.BLUE)
                .addMessage(message)
                .sendToPlayer();
    }

    protected List<String> getTabCompleteOptions(final Collection<String> options, final String start) {
        return this.getTabCompleteOptions(options.toArray(new String[0]), start);
    }

    protected List<String> getTabCompleteOptions(final String[] options, final String start) {
        return DataUtilities.getStartWithIgnoreCase(options, start);
    }

    protected void setMinArgs(final int minArgs) {
        this.minArgs = minArgs;
    }

    protected List<String> getSyntax() {
        return this.syntax;
    }

    protected void setSyntax(final List<String> syntax) {
        this.syntax = syntax;
    }

    protected String getPrefix() {
        return this.prefix;
    }

    protected void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
}
