/*
 * Copyright (C) 2025  Tete
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
import com.trs.hudman.util.annotations.RegistrableHudElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

@RegistrableHudElement(regName = "compass")
@Environment(EnvType.CLIENT)
public class CompassElement extends AbstractHudElement
{
    public Component text = Component.empty();

    protected final boolean center;
    protected final boolean raw;

    public CompassElement(AbstractHudElement root, Minecraft client, Vec2i rCords, JsonConfigHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);

        if (super.hasStringOption("bCenter"))
        {
            this.center = getStringOptionAs("bCenter", Boolean::parseBoolean);
        }
        else
        {
            this.center = true;
        }

        if (super.hasStringOption("bRaw"))
        {
            this.raw = getStringOptionAs("bRaw", Boolean::parseBoolean);
        }
        else
        {
            this.raw = true;
        }
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        if (!center) {
            guiGraphics.drawCenteredString(gui.getFont(), text, getCords().x(), getCords().y(), 0xFFFFFF);
        } else {
            guiGraphics.drawCenteredString(gui.getFont(), text, guiGraphics.guiWidth() / 2, getCords().y(), 0xFFFFFF);
        }
    }

    @Override
    public void tick()
    {
        Direction direction = getPlayer().getDirection();
        String directionText;

        // Map direction to text
        switch (direction)
        {
            case NORTH -> directionText = "North";
            case SOUTH -> directionText = "South";
            case WEST -> directionText = "West";
            case EAST -> directionText = "East";
            default -> directionText = "Invalid";
        }

        // Append rotation if raw is true
        if (raw)
        {
            int rotation = (int) (((getPlayer().getYRot() % 360 + 360) % 360));
            directionText = directionText + "(" + rotation + ")";
        }

        this.text = Component.literal(directionText);
    }
}
