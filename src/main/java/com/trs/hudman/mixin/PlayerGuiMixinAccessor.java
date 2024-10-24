package com.trs.hudman.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
@Environment(EnvType.CLIENT)
public interface PlayerGuiMixinAccessor
{
    /*private int screenWidth;
    private int screenHeight;*/
    @Accessor("screenWidth")
    int GetScreenWidth();

    @Accessor("screenHeight")
    int GetScreenHeight();
}
