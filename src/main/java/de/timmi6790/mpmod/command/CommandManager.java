package de.timmi6790.mpmod.command;

import de.timmi6790.mpmod.events.mineplex.MineplexServerJoinEvent;
import de.timmi6790.mpmod.utilities.EventUtilities;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private final List<ICommand> mineplexOnlyCommands = new ArrayList<>();

    public CommandManager() {
        EventUtilities.registerEvents(this);
    }

    @SubscribeEvent
    public void onMineplexJoin(final MineplexServerJoinEvent event) {
        for (final ICommand command : this.mineplexOnlyCommands) {
            ClientCommandHandler.instance.registerCommand(command);
        }
    }

    @SubscribeEvent
    public void onServerLeave(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        this.removeCommands(this.mineplexOnlyCommands.toArray(new ICommand[0]));
    }

    public void addCommands(final ICommand... commands) {
        for (final ICommand command : commands) {
            this.addCommand(command);
        }
    }

    public void addCommand(final ICommand command) {
        if (MineplexOnlyCommand.class.isAssignableFrom(command.getClass())) {
            this.mineplexOnlyCommands.add(command);

        } else {
            ClientCommandHandler.instance.registerCommand(command);
        }
    }

    public void removeCommands(final ICommand... commands) {
        for (final ICommand command : commands) {
            this.removeCommand(command);
        }
    }

    public void removeCommand(final ICommand command) {
        final Map<String, ICommand> commandMap = ClientCommandHandler.instance.getCommands();
        commandMap.remove(command.getCommandName());

        for (final String commandAlias : command.getCommandAliases()) {
            if (command.equals(commandMap.get(commandAlias))) {
                commandMap.remove(commandAlias);
            }
        }
    }
}
