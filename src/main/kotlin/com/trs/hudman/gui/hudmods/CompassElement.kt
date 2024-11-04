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
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component

@Environment(EnvType.CLIENT)
open class CompassElement(root: AbstractHudElement?, client: Minecraft, rCords: Vec2i, jsonElement: JsonConfgHudElement) :
    AbstractHudElement(root, client, rCords, jsonElement)
{
    @JvmField
    var Text: Component = Component.literal("test")

    @JvmField
    protected var noCenter: FLAG = super.jsonElement.strings.first() == "!center"

    @JvmField
    protected var raw: FLAG = run<FLAG> RET@{
        var rl = false
        for (str: String in super.jsonElement.strings())
        {
            rl = str == "raw"
        }
        return@RET rl
    }

    override fun render(partialTick: Float, guiGraphics: GuiGraphics, gui: Gui)
    {
        if (noCenter)
        {
            guiGraphics.drawCenteredString(gui.font, Text, jsonElement.cords.x, jsonElement.cords.y, 0xFFFFFF)
        }
        else
        {
            guiGraphics.drawCenteredString(gui.font, Text, guiGraphics.guiWidth() / 2, jsonElement.cords.y, 0xFFFFFF)
        }

    }

    override fun tick()
    {
        val direction: Direction = player.direction

        var string2 = "test";

        string2 = when (direction) {
            Direction.NORTH -> "North";
            Direction.SOUTH -> "South";
            Direction.WEST -> "West";
            Direction.EAST -> "East";
            else -> "Invalid";
        };

        if (raw)
        {
            string2 =  "$string2(${(((player.yRot) % 360 + 360) % 360).toInt()})"
        }


        this.Text = Component.literal(string2)
    }
}
