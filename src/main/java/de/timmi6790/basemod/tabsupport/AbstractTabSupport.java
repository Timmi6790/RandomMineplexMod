package de.timmi6790.basemod.tabsupport;

import de.timmi6790.basemod.utilities.DataUtilities;
import de.timmi6790.basemod.utilities.PlayerUtilities;
import lombok.Data;

import java.util.*;

@Data
public abstract class AbstractTabSupport {
    private final List<String> commands = new ArrayList<>();

    public AbstractTabSupport(final String... commands) {
        this.addCommands(commands);
    }

    public abstract void onTabSupport(TabSupportData tabSupportData);

    public void addCommands(final String... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    protected static String[] generateIntArray(final int startIncluded, final int endIncluded) {
        final List<String> values = new ArrayList<>();
        for (int count = startIncluded; endIncluded >= count; count++) {
            values.add(String.valueOf(count));
        }
        return values.toArray(new String[0]);
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
