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
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class TextElement extends AbstractHudElement
{

    private final boolean isCenter;
    private final Component text;

    public TextElement(AbstractHudElement root, Minecraft client, Vec2i rCords, JsonConfigHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);

        // Initialize isCenter by checking if "center" is in jsonElement's strings
        boolean centerFlag = false;
        for (String str : super.getJsonElement().strings())
        {
            if (str.equalsIgnoreCase("center"))
            {
                centerFlag = true;
                break;
            }
        }
        this.isCenter = centerFlag;

        // Initialize text with the first string in jsonElement or a default if not present
        Component tempText;
        try
        {
            tempText = Component.literal(super.getJsonElement().strings().get(0));
        }
        catch (Exception ex)
        {
            tempText = Component.literal("'NO STR'");
        }
        this.text = tempText;
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        int x = isCenter ? guiGraphics.guiWidth() / 2 : getCords().x();
        int y = getCords().y();
        guiGraphics.drawCenteredString(gui.getFont(), text, x, y, 0xFFFFFF);
    }

    @Override
    public void tick() {
    }
}

