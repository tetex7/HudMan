package com.trs.hudman.gui.hudmods

import com.trs.hudman.confg.JsonConfgHudElement
import com.trs.hudman.qlang.FLAG
import io.github.cottonmc.cotton.gui.widget.data.Vec2i
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component

@Environment(EnvType.CLIENT)
open class CompassElement(root: HudElement?, client: Minecraft, rCords: Vec2i, jsonElement: JsonConfgHudElement) :
    HudElement(root, client, rCords, jsonElement)
{
    @JvmField
    var Text: Component = Component.literal("test")

    @JvmField
    protected var noCenter: FLAG = super.jsonElementl.Strs.first() == "!center"

    @JvmField
    protected var raw: FLAG = run<FLAG> RET@{
        var rl = false
        for (str: String in super.jsonElementl.Strs())
        {
            rl = str == "raw"
        }
        return@RET rl
    }

    override fun render(pPartialTick: Float, pGuiGraphics: GuiGraphics, gui: Gui)
    {
        if (noCenter)
        {
            pGuiGraphics.drawCenteredString(gui.font, Text, jsonElementl.Cords.x, jsonElementl.Cords.y, 0xFFFFFF)
        }
        else
        {
            pGuiGraphics.drawCenteredString(gui.font, Text, pGuiGraphics.guiWidth() / 2, jsonElementl.Cords.y, 0xFFFFFF)
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
            string2 =  "$string2(${(((player.yRot) % 360 + 360) % 360).toInt()})"//.toInt()})" //(playr.getYRot().toInt() / 2).toInt()})"
        }


        this.Text = Component.literal(string2)
    }
}
