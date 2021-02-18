package de.timmi6790.basemod;

import de.timmi6790.basemod.values.mineplex.MineplexGames;
import lombok.Data;


@Data
public class ModCache {
    private String currentServer;
    private MineplexGames currentGame;
    private boolean onMineplex;
    private boolean onMps;

    public String getCurrentServerGroup() {
        final int splitPosition = this.currentServer.indexOf('-');
        if (splitPosition == -1) {
            return this.currentServer;
        }

        return this.currentServer.substring(0, splitPosition);
    }
}
