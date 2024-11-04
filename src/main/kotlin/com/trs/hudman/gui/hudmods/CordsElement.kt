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
import com.trs.hudman.qlang.Qlang
import com.trs.hudman.qlang.Qlang_inst
import com.trs.hudman.util.Vec2i
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import java.util.regex.Pattern


@Environment(EnvType.CLIENT)
open class CordsElement(root: AbstractHudElement?, client: Minecraft, rCords: Vec2i, jsonElement: JsonConfgHudElement) :
    AbstractHudElement(root, client, rCords, jsonElement)
{
    @JvmField
    var Text: Component = Component.literal("test")

    @JvmField
    protected var isCenter: FLAG = run<FLAG> {
        var fl = false
        for (str: String in super.jsonElement.strings())
        {
            fl = str.lowercase() == "center"
        }
        return@run fl
    }

    override fun render(partialTick: Float, guiGraphics: GuiGraphics, gui: Gui)
    {
        if (isCenter)
        {
            guiGraphics.drawCenteredString(gui.font, Text, guiGraphics.guiWidth() / 2, jsonElement.cords.y, 0xFFFFFF)
        }
        else
        {
            guiGraphics.drawCenteredString(gui.font, Text, jsonElement.cords.x, jsonElement.cords.y, 0xFFFFFF)
        }
    }

    override fun tick()
    {
        //if (this.getClient().player != null)
        //{
        val str = run {
            try
            {
                cordlang(super.jsonElement.strings()[0]);
            }
            catch (x: Exception)
            {
                cordlang("X(%X%), Z(%Z%)), Y(%Y%)");
            }
        }
        val XyZ = str.first
        Text = Component.literal(XyZ)
    //}
    }

    val cordlang = Qlang.Builder().add(X_TAG, Qlang_inst(RET@ { tag: String, p: Pattern, ctxt: String ->
        return@RET this@CordsElement.player!!.getX().toInt().toString();
    })).add(Y_TAG, Qlang_inst(RET@ { tag: String, p: Pattern, ctxt: String ->
        return@RET this@CordsElement.player!!.getY().toInt().toString();
    })).add(Z_TAG, Qlang_inst(RET@ { tag: String, p: Pattern, ctxt: String ->
        return@RET this@CordsElement.player!!.getZ().toInt().toString();
    })).bulid(false)

    companion object
    {
        const val X_TAG: String = "%X%"
        const val Y_TAG: String = "%Y%"
        const val Z_TAG: String = "%Z%"

    }
}
