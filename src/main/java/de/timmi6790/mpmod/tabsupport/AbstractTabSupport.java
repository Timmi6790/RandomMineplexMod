package de.timmi6790.mpmod.tabsupport;

import de.timmi6790.mpmod.utilities.DataUtilities;
import de.timmi6790.mpmod.utilities.PlayerUtilities;
import lombok.Data;

import java.util.*;

@Data
public abstract class AbstractTabSupport {
    private final List<String> commands;
    private boolean cancelServerRequest = false;

    public AbstractTabSupport(final String... commands) {
        this.commands = new ArrayList<>(Arrays.asList(commands));
    }

    public abstract void onTabSupport(TabSupportData tabSupportData);

    public void addCommands(final String... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    protected Set<String> getAllPlayersInLobby() {
        return PlayerUtilities.getAllPlayersInLobby();
    }

    protected List<String> getStartWithIgnoreCase(final String[] values, final String startWith) {
        return DataUtilities.getStartWithIgnoreCase(values, startWith);
    }

    protected List<String> getStartWithIgnoreCase(final Collection<String> values, final String startWith) {
        return DataUtilities.getStartWithIgnoreCase(values, startWith);
    }
}
