package de.timmi6790.mpmod.tabsupport.tabsupports;

import de.timmi6790.mpmod.tabsupport.AbstractTabSupport;
import de.timmi6790.mpmod.tabsupport.TabSupportData;

public class PlayerNamesAddTabSupport extends AbstractTabSupport {
    public PlayerNamesAddTabSupport(final String... commands) {
        super(commands);
    }

    @Override
    public void onTabSupport(final TabSupportData tabSupportData) {
        final String[] args = tabSupportData.getMessageArgs();
        if (args.length != 2) {
            return;
        }

        tabSupportData.getTabReturn().addAll(this.getStartWithIgnoreCase(this.getAllPlayersInLobby(), args[1]));
    }
}
