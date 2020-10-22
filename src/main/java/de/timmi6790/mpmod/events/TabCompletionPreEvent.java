package de.timmi6790.mpmod.events;

import de.timmi6790.mpmod.tabsupport.TabSupportData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TabCompletionPreEvent extends Event {
    private final TabSupportData tabSupportData;

    public String getCommandName() {
        return this.tabSupportData.getMessageArgs()[0];
    }

    public boolean isCommand() {
        return this.tabSupportData.getMessageArgs()[0].startsWith("/");
    }
}
