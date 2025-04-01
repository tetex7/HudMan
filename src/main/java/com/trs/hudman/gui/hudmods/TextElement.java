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
import com.trs.qlang.Qlang;
import com.trs.qlang.QlangInstruction;
import com.trs.qlang.QlangString;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.regex.Pattern;

@RegistrableHudElement(regName = "text")
@Environment(EnvType.CLIENT)
public class TextElement extends AbstractHudElement
{

    private final boolean isCenter;
    private final QlangString text;

    public TextElement(AbstractHudElement root, Minecraft client, Vec2i rCords, JsonConfigHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);

        // Initialize isCenter by checking if "center" is in jsonElement's strings
        //this.isCenter = !getJsonElement().strings().isEmpty() && getJsonElement().strings().get(getJsonElement().strings().size() - 1).equalsIgnoreCase("center");


        if (super.hasStringOption("bCenter"))
        {
            this.isCenter = getStringOptionAs("bCenter", Boolean::parseBoolean);
        }
        else
        {
            this.isCenter = false;
        }

        this.text = getElementConfigText();
        text.getQLangInterpreter().AddInstruction(
                "%USER%",
                    QlangInstruction.of((String tag, Pattern pattern) -> getClient().getUser().getName()
                )
        );
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        int x = isCenter ? guiGraphics.guiWidth() / 2 : getCords().x();
        int y = getCords().y();
        guiGraphics.drawCenteredString(gui.getFont(), Component.literal(text.toString()), x, y, getConfColor().toRgbInt());
    }

    @Override
    public void tick() {}

    public final QlangString getElementConfigText()
    {
        if (hasStringOption("sText"))
        {
            return QlangString.ofLiteral(getStringOption("sText"));
        }

        boolean badStringFlag = (getJsonElement().strings().size() == 1) && getJsonElement().strings().get(0).startsWith("bCenter");

        // Initialize text with the first string in jsonElement or a default if not present
        String tempText;
        if (badStringFlag)
        {
            tempText = "%USER% 'NO STR'";
        }
        else
        {
            try
            {
                tempText = super.getJsonElement().strings().get(0);
            }
            catch (Exception ex)
            {
                tempText = "%USER% 'NO STR'";
            }
        }
        return QlangString.ofLiteral(tempText);
    }
}

