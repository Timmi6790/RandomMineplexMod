package de.timmi6790.basemod.events.mineplex;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MineplexServerJoinEvent extends Event {
    private final String lobbyName;

    public MineplexServerJoinEvent(final String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return this.lobbyName;
    }
}
