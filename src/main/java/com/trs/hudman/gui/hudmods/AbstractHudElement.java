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

    public final Minecraft getClient()
    {
        return this.client;
    }

    public final JsonConfigHudElement getJsonElement()
    {
        return this.jsonElement;
    }

    public final AbstractHudElement getRoot()
    {
        return this.root;
    }

    public final LocalPlayer getPlayer()
    {
        return this.player;
    }

    public final Vec2i getCords()
    {
        return this.cords;
    }

    public final UUID getElementUUID()
    {
        return elementUUID;
    }

    public abstract void render(float partialTick, GuiGraphics guiGraphics, Gui gui);
    public abstract void tick();
}
