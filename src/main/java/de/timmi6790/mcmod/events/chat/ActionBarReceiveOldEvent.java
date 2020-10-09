package de.timmi6790.mcmod.events.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ActionBarReceiveOldEvent extends Event {
    private IChatComponent message;
}
