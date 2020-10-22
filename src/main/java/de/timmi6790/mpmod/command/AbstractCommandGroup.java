package de.timmi6790.mpmod.command;

import de.timmi6790.mpmod.utilities.MessageBuilder;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public abstract class AbstractCommandGroup extends AbstractCommand {
    private final List<AbstractCommand> subCommands = new ArrayList<>();
    private String[][] tabCompleteOptions = new String[0][0];

    public AbstractCommandGroup(final String commandName) {
        super(commandName);
    }

    protected Optional<AbstractCommand> getSubCommand(final String name) {
        for (final AbstractCommand command : this.subCommands) {
            if (command.getCommandName().equalsIgnoreCase(name)) {
                return Optional.of(command);
            }

            for (final String alias : command.getCommandAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return Optional.of(command);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        if (args.length == 0) {
            final MessageBuilder helpMessage = new MessageBuilder(StringUtils.capitalize(this.getCommandName()) + "-Command\n", EnumChatFormatting.GOLD);
            for (final AbstractCommand command : this.subCommands) {
                helpMessage.addMessage("\n" + "/" + this.getCommandName() + " " + command.getCommandName() + " " + String.join(" ", command.getSyntax()), EnumChatFormatting.YELLOW);
            }
            helpMessage.sendToPlayerWithBox();
            return;
        }

        final Optional<AbstractCommand> command = this.getSubCommand(args[0]);
        if (!command.isPresent()) {
            this.returnTellNotValidCommand(args[0]);
            return;
        }
        command.get().processCommand(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            final List<String> tabCompletion = new ArrayList<>(this.getTabCompleteOptions(this.getTabCompleteOptions(0), args[0]));

            final String argLower = args[0].toLowerCase();
            for (final AbstractCommand command : this.subCommands) {
                if (command.getCommandName().toLowerCase().startsWith(argLower)) {
                    tabCompletion.add(command.getCommandName());
                }
                tabCompletion.addAll(this.getTabCompleteOptions(command.getCommandAliases(), args[0]));
            }
            return tabCompletion;
        }

        final Optional<AbstractCommand> command = this.getSubCommand(args[0]);
        if (command.isPresent()) {
            return command.get().addTabCompletionOptions(sender, Arrays.copyOfRange(args, 1, args.length), pos);
        }

        return new ArrayList<>();
    }

    protected final void registerSubCommand(final AbstractCommand command) {
        // Set Group prefix if no own prefix
        if (command.getPrefix() == null && this.getPrefix() != null) {
            command.setPrefix(this.getPrefix());
        }
        this.subCommands.add(command);
    }

    protected final void registerSubCommands(final AbstractCommand... commands) {
        for (final AbstractCommand command : commands) {
            this.registerSubCommand(command);
        }
    }

    private String[] getTabCompleteOptions(final int position) {
        try {
            return this.tabCompleteOptions[position];
        } catch (final ArrayIndexOutOfBoundsException ignore) {
            return new String[0];
        }
    }

    protected final void registerTabCompleteOption(final int position, final String option) {
        String[] options = this.getTabCompleteOptions(position);

        boolean fullArray = true;
        for (int index = 0; index < options.length; index++) {
            if (options[index] == null) {
                options[index] = option;
                fullArray = false;
                break;
            }
        }

        if (fullArray) {
            options = Arrays.copyOf(options, options.length + 1);
            options[options.length - 1] = option;
        }

        if (this.tabCompleteOptions.length - 1 < position) {
            this.tabCompleteOptions = Arrays.copyOf(this.tabCompleteOptions, position + 1);
        }

        this.tabCompleteOptions[position] = options;
    }

    protected final void registerTabCompleteOptions(final int position, final String... options) {
        for (final String option : options) {
            this.registerTabCompleteOption(position, option);
        }
    }

    @Override
    protected List<String> getSyntax() {
        // Only show the first argument instead of the entire syntax
        final List<String> subCommandNames = new ArrayList<>();
        for (final AbstractCommand subCommand : this.subCommands) {
            subCommandNames.add(subCommand.getCommandName());
        }
        return Collections.singletonList(String.join("|", subCommandNames));
    }

    public List<AbstractCommand> getSubCommands() {
        return this.subCommands;
    }
}
