package de.timmi6790.basemod.tabsupport.tabsupports;

import de.timmi6790.basemod.tabsupport.AbstractTabSupport;
import de.timmi6790.basemod.tabsupport.TabSupportData;

public class PlayerNamesTabSupport extends AbstractTabSupport {
    public PlayerNamesTabSupport(final String... commands) {
        super(commands);
    }

    @Override
    public void onTabSupport(final TabSupportData tabSupportData) {
        final String[] args = tabSupportData.getMessageArgs();
        if (args.length == 2) {
            tabSupportData.getTabReturn().addAll(
                    this.getStartWithIgnoreCase(
                            this.getAllPlayersInLobby(),
                            args[1]
                    )
            );
        }
    }
}
