package com.trs.hudman.gui.hudmods

import com.trs.hudman.confg.JsonConfgHudElement
import com.trs.hudman.qlang.FLAG
import com.trs.hudman.qlang.Qlang
import com.trs.hudman.qlang.Qlang_inst
import io.github.cottonmc.cotton.gui.widget.data.Vec2i
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import java.util.regex.Pattern


@Environment(EnvType.CLIENT)
open class CordsElement(root: HudElement?, client: Minecraft, rCords: Vec2i, jsonElement: JsonConfgHudElement) :
    HudElement(root, client, rCords, jsonElement)
{
    @JvmField
    var Text: Component = Component.literal("test")

    @JvmField
    protected var isCenter: FLAG = run<FLAG> {
        var fl = false
        for (str: String in super.jsonElementl.Strs())
        {
            fl = str.lowercase() == "center"
        }
        return@run fl
    }

    override fun render(pPartialTick: Float, pGuiGraphics: GuiGraphics, gui: Gui)
    {
        if (isCenter)
        {
            pGuiGraphics.drawCenteredString(gui.font, Text, pGuiGraphics.guiWidth() / 2, jsonElementl.Cords.y, 0xFFFFFF)
        }
        else
        {
            pGuiGraphics.drawCenteredString(gui.font, Text, jsonElementl.Cords.x, jsonElementl.Cords.y, 0xFFFFFF)
        }
    }

    override fun tick()
    {
        //if (this.getClient().player != null)
        //{
        val str = run {
            try
            {
                cordlang(super.jsonElementl.Strs()[0]);
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
        return@RET this@CordsElement.client.player!!.getX().toInt().toString();
    })).add(Y_TAG, Qlang_inst(RET@ { tag: String, p: Pattern, ctxt: String ->
        return@RET this@CordsElement.client.player!!.getY().toInt().toString();
    })).add(Z_TAG, Qlang_inst(RET@ { tag: String, p: Pattern, ctxt: String ->
        return@RET this@CordsElement.client.player!!.getZ().toInt().toString();
    })).bulid(false)

    companion object
    {
        const val X_TAG: String = "%X%"
        const val Y_TAG: String = "%Y%"
        const val Z_TAG: String = "%Z%"

    }
}
