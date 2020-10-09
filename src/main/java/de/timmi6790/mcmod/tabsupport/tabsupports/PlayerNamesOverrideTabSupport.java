package de.timmi6790.mcmod.tabsupport.tabsupports;

import de.timmi6790.mcmod.tabsupport.AbstractTabSupport;
import de.timmi6790.mcmod.tabsupport.TabSupportData;
import de.timmi6790.mcmod.utilities.DataUtilities;

public class PlayerNamesOverrideTabSupport extends AbstractTabSupport {
    public PlayerNamesOverrideTabSupport(final String... commands) {
        super(commands);
        this.setCancelServerRequest(true);
    }

    @Override
    public void onTabSupport(final TabSupportData tabSupportData) {
        final String[] args = tabSupportData.getMessageArgs();
        if (args.length != 2) {
            return;
        }

        tabSupportData.setTabReturn(DataUtilities.getStartWithIgnoreCase(this.getAllPlayersInLobby(), args[1]));
    }
}
