package de.timmi6790.mcmod.events.mineplex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraftforge.fml.common.eventhandler.Event;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class MineplexServerChangeEvent extends Event {
    private final String oldLobbyName;
    private final String newLobbyName;
}
