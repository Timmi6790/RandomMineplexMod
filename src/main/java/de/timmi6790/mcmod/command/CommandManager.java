package de.timmi6790.mcmod.command;

import de.timmi6790.mcmod.events.mineplex.MineplexServerJoinEvent;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private final List<ICommand> mineplexOnlyCommands = new ArrayList<>();
    private final Map<String, ICommand> registeredCommands = new HashMap<>();
    private final Map<String, String> registeredCommandsAlias = new HashMap<>();

    @SubscribeEvent
    public void onMineplexJoin(final MineplexServerJoinEvent event) {
        for (final ICommand command : this.mineplexOnlyCommands) {
            ClientCommandHandler.instance.registerCommand(command);
        }
    }

    @SubscribeEvent
    public void onServerLeave(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        final Map<String, ICommand> commandMap = ClientCommandHandler.instance.getCommands();

        this.mineplexOnlyCommands.forEach(command -> {
            commandMap.remove(command.getCommandName());

            command.getCommandAliases().stream()
                    .filter(alias -> {
                        final ICommand icommand = commandMap.get(alias);
                        return icommand != null && icommand.equals(command);
                    })
                    .forEach(commandMap::remove);
        });
    }

    public void addCommands(final ICommand... commands) {
        for (final ICommand command : commands) {
            final String commandNameLower = command.getCommandName().toLowerCase();
            this.registeredCommands.put(commandNameLower, command);
            for (final String alias : command.getCommandAliases()) {
                this.registeredCommandsAlias.put(alias.toLowerCase(), commandNameLower);
            }

            if (MineplexOnlyCommand.class.isAssignableFrom(command.getClass())) {
                this.mineplexOnlyCommands.add(command);

            } else {
                ClientCommandHandler.instance.registerCommand(command);
            }
        }
    }
}
