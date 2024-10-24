package com.trs.hudman.gui.hudmods

import com.trs.hudman.confg.JsonConfgHudElement
import com.trs.hudman.qlang.FLAG
import io.github.cottonmc.cotton.gui.widget.data.Vec2i
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

@Environment(EnvType.CLIENT)
class TextElement(root: HudElement?, client: Minecraft, rCords: Vec2i, jsonElement: JsonConfgHudElement) :
    HudElement(root, client, rCords, jsonElement)
{

    //@JvmField
    private var isCenter: FLAG = run<FLAG> {
        var fl = false
        for (str: String in super.jsonElementl.Strs())
        {
            fl = str.lowercase() == "center"
        }
        return@run fl
    }

    private val text: Component = run<Component> {
        try
        {
            Component.literal(super.jsonElementl.Strs()[0])
        }
        catch (ex: Exception)
        {
            Component.literal("\'NO STR\'")
        }

    }


    override fun render(pPartialTick: Float, pGuiGraphics: GuiGraphics, gui: Gui)
    {
        if (isCenter)
        {
            pGuiGraphics.drawCenteredString(gui.font, text, pGuiGraphics.guiWidth() / 2, jsonElementl.Cords.y, 0xFFFFFF)
            //pGuiGraphics.drawCenteredString()
        }
        else
        {
            pGuiGraphics.drawCenteredString(gui.font, text, jsonElementl.Cords.x, jsonElementl.Cords.y, 0xFFFFFF)
        }
    }

    override fun tick() {}
}