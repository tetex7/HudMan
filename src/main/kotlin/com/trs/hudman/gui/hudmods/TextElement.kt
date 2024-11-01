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

package com.trs.hudman.gui.hudmods

import com.trs.hudman.confg.JsonConfgHudElement
import com.trs.hudman.qlang.FLAG
import com.trs.hudman.util.Vec2i
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

@Environment(EnvType.CLIENT)
class TextElement(root: AbstractHudElement?, client: Minecraft, rCords: Vec2i, jsonElement: JsonConfgHudElement) :
    AbstractHudElement(root, client, rCords, jsonElement)
{

    //@JvmField
    private var isCenter: FLAG = run<FLAG> {
        var fl = false
        for (str: String in super.jsonElementl.strs())
        {
            fl = str.lowercase() == "center"
        }
        return@run fl
    }

    private val text: Component = run<Component> {
        try
        {
            Component.literal(super.jsonElementl.strs()[0])
        }
        catch (ex: Exception)
        {
            Component.literal("\'NO STR\'")
        }

    }


    override fun render(partialTick: Float, guiGraphics: GuiGraphics, gui: Gui)
    {
        if (isCenter)
        {
            guiGraphics.drawCenteredString(gui.font, text, guiGraphics.guiWidth() / 2, jsonElementl.cords.y, 0xFFFFFF)
            //pGuiGraphics.drawCenteredString()
        }
        else
        {
            guiGraphics.drawCenteredString(gui.font, text, jsonElementl.cords.x, jsonElementl.cords.y, 0xFFFFFF)
        }
    }

    override fun tick() {}
}