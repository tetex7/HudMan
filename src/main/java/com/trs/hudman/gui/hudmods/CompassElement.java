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
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class CompassElement extends AbstractHudElement
{
    public Component text = Component.literal("test");

    protected final boolean noCenter;
    protected final boolean raw;

    public CompassElement(AbstractHudElement root, Minecraft client, Vec2i rCords, JsonConfigHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);

        // Initialize noCenter flag based on jsonElement's strings
        this.noCenter = super.getJsonElement().strings().get(0).equals("!center");

        // Initialize raw flag by checking if "raw" is in the strings list
        boolean rawFlag = false;
        for (String str : super.getJsonElement().strings()) {
            if (str.equals("raw")) {
                rawFlag = true;
                break;
            }
        }
        this.raw = rawFlag;
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        if (noCenter) {
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
        switch (direction) {
            case NORTH -> directionText = "North";
            case SOUTH -> directionText = "South";
            case WEST -> directionText = "West";
            case EAST -> directionText = "East";
            default -> directionText = "Invalid";
        }

        // Append rotation if raw is true
        if (raw) {
            int rotation = (int) (((getPlayer().getYRot() % 360 + 360) % 360));
            directionText = directionText + "(" + rotation + ")";
        }

        this.text = Component.literal(directionText);
    }
}
