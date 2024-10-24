package com.trs.hudman.gui.hudmods;

import com.mojang.blaze3d.vertex.PoseStack;
import com.trs.hudman.confg.JsonConfgHudElement;
import io.github.cottonmc.cotton.gui.widget.data.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class Hud3dElement extends HudElement
{
    public Hud3dElement(@Nullable HudElement root, @NotNull Minecraft client, @NotNull Vec2i rCords, @NotNull JsonConfgHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);
    }

    @Override
    public void render(float pPartialTick, GuiGraphics pGuiGraphics, Gui gui)
    {
        render3d(pGuiGraphics.pose(), pGuiGraphics, gui);
        render2d(pGuiGraphics, gui);
    }

    public abstract void render3d(PoseStack matrixStack, GuiGraphics pGuiGraphics, Gui gui);
    public abstract void render2d(GuiGraphics pGuiGraphics, Gui gui);
}
