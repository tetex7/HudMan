package com.trs.hudman.gui.hudmods;

import com.trs.hudman.confg.JsonConfgHudElement;
import io.github.cottonmc.cotton.gui.widget.data.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.Level;

@Environment(EnvType.CLIENT)
public class FPSElement extends HudElement
{
    private int fps = 0;


    public FPSElement(@Nullable HudElement root, @NotNull Minecraft client, @NotNull Vec2i rCords, @NotNull JsonConfgHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);
    }

    @Override
    public void render(float pPartialTick, GuiGraphics pGuiGraphics, Gui gui)
    {
        Component Text = Component.literal("FPS: " + fps);
        pGuiGraphics.drawCenteredString(gui.getFont(), Text, jsonElementl.Cords().x(), jsonElementl.Cords().y(), 0xFFFFFF);
    }


    @Override
    public void tick()
    {
        fps = getClient().getFps();
    }
}
