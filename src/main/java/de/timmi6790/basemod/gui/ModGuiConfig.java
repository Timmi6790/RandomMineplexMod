package de.timmi6790.basemod.gui;

import de.timmi6790.basemod.McMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ModGuiConfig extends GuiConfig {
    public ModGuiConfig(final GuiScreen guiScreen) {
        super(
                guiScreen,
                new ConfigElement(McMod.getInstance().getConfiguration().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                McMod.getInstance().getModId(),
                false,
                true,
                GuiConfig.getAbridgedConfigPath(McMod.getInstance().getConfiguration().toString())
        );
    }
}