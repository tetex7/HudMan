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
import com.trs.qlang.Qlang;
import com.trs.qlang.QlangInstruction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class TextElement extends AbstractHudElement
{

    private final boolean isCenter;
    private final String text;

    Qlang parser = Qlang.builder()
            .includesStandardLibrary()
            .AddInstruction("%USER%", QlangInstruction.of((String tag, Pattern pattern) -> getClient().getUser().getName()))
            .build();

    public TextElement(AbstractHudElement root, Minecraft client, Vec2i rCords, JsonConfigHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);

        // Initialize isCenter by checking if "center" is in jsonElement's strings
        this.isCenter = getJsonElement().strings().get(getJsonElement().strings().size() - 1).equalsIgnoreCase("center");

        boolean badStringFlag = (getJsonElement().strings().size() == 1) && getJsonElement().strings().get(0).equalsIgnoreCase("center");

        // Initialize text with the first string in jsonElement or a default if not present
        String tempText;
        if (badStringFlag)
        {
            tempText = "'NO STR'";
        }
        else
        {
            try
            {
                tempText = super.getJsonElement().strings().get(0);
            }
            catch (Exception ex)
            {
                tempText = "'NO STR'";
            }
        }
        this.text = tempText;
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        int x = isCenter ? guiGraphics.guiWidth() / 2 : getCords().x();
        int y = getCords().y();
        guiGraphics.drawCenteredString(gui.getFont(), Component.literal(parser.parse(text).outputString()), x, y, 0xFFFFFF);
    }

    @Override
    public void tick() {
    }
}

