package com.trs.hudman.gui.hudmods;

import com.trs.hudman.confg.JsonConfgHudElement;
import io.github.cottonmc.cotton.gui.widget.data.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class HudElement// extends Screen
{
    protected final HudElement Root;
    protected final LocalPlayer Player;
    protected final Vec2i Cords;
    protected final Minecraft Client;
    protected final JsonConfgHudElement jsonElementl;

    public HudElement(@Nullable HudElement root, @NotNull Minecraft client, @NotNull Vec2i rCords, @NotNull JsonConfgHudElement jsonElement)
    {
        this.Root = root;
        this.Player = client.player;
        this.Cords = rCords;
        this.Client = client;
        this.jsonElementl = jsonElement;
    }

    public Minecraft getClient()
    {
        return Client;
    }

    public JsonConfgHudElement getJsonElementl()
    {
        return this.jsonElementl;
    }

    public HudElement getRoot()
    {
        return Root;
    }

    public Player getPlayer()
    {
        return Player;
    }

    public Vec2i getCords()
    {
        return Cords;
    }


    public abstract void render(float pPartialTick, GuiGraphics pGuiGraphics, Gui gui);
    public abstract void tick();
}
