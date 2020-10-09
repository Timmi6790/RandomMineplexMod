package de.timmi6790.mcmod.gui;

import de.timmi6790.mcmod.McMod;
import de.timmi6790.mcmod.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ModGuiConfig extends GuiConfig {
    public ModGuiConfig(final GuiScreen guiScreen) {
        super(
                guiScreen,
                new ConfigElement(McMod.getConfiguration().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                Reference.MODID,
                false,
                true,
                GuiConfig.getAbridgedConfigPath(McMod.getConfiguration().toString())
        );
    }
}