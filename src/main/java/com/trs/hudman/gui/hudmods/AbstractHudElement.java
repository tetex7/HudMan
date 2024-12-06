/*
 * Copyright (C) 2024  Tete
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.trs.hudman.gui.hudmods;

import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.util.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * The super class for all HUD elements
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractHudElement implements IRenderPrimitive
{
    private final AbstractHudElement root;
    private final LocalPlayer player;
    private final Vec2i cords;
    private final Minecraft client;
    private final JsonConfigHudElement jsonElement;
    private final UUID elementUUID = UUID.randomUUID();
    private final int intID = 0;

    /**
     *
     * @param root Mostly time it's null and will probably be removed
     * @param client The current Minecraft client
     * @param cords The coordinates of the element on the user screen
     * @param jsonElement The Jason config structure turned into a Java class
     * @implSpec Your constructor using this super class must contain all four
     */
    public AbstractHudElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i cords, @NotNull JsonConfigHudElement jsonElement)
    {
        Objects.requireNonNull(client);
        Objects.requireNonNull(cords);
        Objects.requireNonNull(jsonElement);
        this.root = root;
        this.player = client.player;
        this.cords = cords;
        this.client = client;
        this.jsonElement = jsonElement;
    }

    /**
     * @return Provides a reference to the client
     */
    public final Minecraft getClient()
    {
        return this.client;
    }

    /**
     * @return Provides a reference to the Jsonc onfig Element
     * @see JsonConfigHudElement
     */
    public final JsonConfigHudElement getJsonElement()
    {
        return this.jsonElement;
    }

    /**
     * Irrelevant do not use
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    public final AbstractHudElement getRoot()
    {
        return this.root;
    }

    /**
     * @return Provides information about the current user player
     */
    public final LocalPlayer getPlayer()
    {
        return this.player;
    }

    /**
     * @return Coordinates where the element is to be rendered on the user screen
     */
    public final Vec2i getCords()
    {
        return this.cords;
    }

    /**
     * UUID is used for element identification
     * @return Provides the elements UID
     */
    public final UUID getElementUUID()
    {
        return elementUUID;
    }

    /**
     * Where all your graphical code lies for your element
     * @param partialTick The delta between ticks (I really don't know, and I've really never used this)
     * @param guiGraphics Minecraft's facilities to render to the screen
     * @param gui a reference to the player GUI
     */
    public abstract void render(float partialTick, GuiGraphics guiGraphics, Gui gui);

    /**
     * A place for your elements logic
     * @apiNote This function is run every gui tick
     */
    public abstract void tick();
}
