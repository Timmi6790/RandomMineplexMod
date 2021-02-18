package de.timmi6790.basemod.tabsupport;

import de.timmi6790.basemod.utilities.EventUtilities;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TabSupportManager {
    private final Map<String, List<AbstractTabSupport>> tabSupports = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @Getter
    private final List<AbstractTabSupport> allTabSupportListeners = new ArrayList<>();

    public TabSupportManager() {
        EventUtilities.registerEvents(
                new TabSupportListener(this)
        );
    }

    public void registerTabSupports(final AbstractTabSupport... tabSupports) {
        for (final AbstractTabSupport tabSupport : tabSupports) {
            final List<String> commands = tabSupport.getCommands();
            // If no commands listen to all
            if (commands.isEmpty()) {
                this.allTabSupportListeners.add(tabSupport);
                continue;
            }

            for (final String command : tabSupport.getCommands()) {
                this.tabSupports
                        .computeIfAbsent(command, k -> new ArrayList<>(1))
                        .add(tabSupport);
            }
        }
    }

    public List<AbstractTabSupport> getTabSupports(final String command) {
        return this.tabSupports.getOrDefault(command, new ArrayList<>());
    }
}
