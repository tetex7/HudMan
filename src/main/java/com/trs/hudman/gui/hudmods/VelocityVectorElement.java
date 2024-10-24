package com.trs.hudman.gui.hudmods;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.trs.hudman.confg.JsonConfgHudElement;
import io.github.cottonmc.cotton.gui.widget.data.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.jna.platform.unix.AixLibc;

@Environment(EnvType.CLIENT)
public class VelocityVectorElement extends HudElement
{
    Vec3 player_velocity = new Vec3(0,0, 0);
    Vec3 player_cords = new Vec3(0, 0, 0);
    Vec3 dta_cords = new Vec3(0, 0, 0);


    public VelocityVectorElement(@Nullable HudElement root, @NotNull Minecraft client, @NotNull Vec2i rCords, @NotNull JsonConfgHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);
    }

    @Override
    public void render(float pPartialTick, GuiGraphics guiGraphics, Gui gui)
    {
    }

    @Override
    public void tick()
    {
        player_velocity = getPlayer().getDeltaMovement();
        player_cords = new Vec3(getPlayer().getX(), getPlayer().getY(), getPlayer().getZ());
        dta_cords = player_cords.add(player_velocity);
    }
}
