package com.trs.hudman

import com.google.gson.Gson
import com.trs.hudman.gui.hudmods.HudElement
import net.minecraft.client.Minecraft
import org.slf4j.LoggerFactory
import java.util.*
import com.trs.hudman.confg.JsonConfgHudFile
import com.trs.hudman.util.INamespaceHandler
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import org.slf4j.Logger
import java.io.File

@Environment(EnvType.CLIENT)
object HudState
{
    @JvmStatic
    val modid = "hudman"

    @JvmStatic
    val hudElements = Stack<HudElement>()

    @JvmStatic
    val LOGGER:Logger = LoggerFactory.getLogger("Hudman")

    @JvmStatic
    var showHud:Boolean = true

    @JvmStatic
    val NamespaceHandlers: HashMap<String, INamespaceHandler> = HashMap()

    @JvmStatic
    fun getCong(): JsonConfgHudFile
    {
        val confpath = "${Minecraft.getInstance().gameDirectory}/hud.json"
        val confgHud: JsonConfgHudFile = Gson().fromJson<JsonConfgHudFile>(File(confpath).readText(Charsets.UTF_8), JsonConfgHudFile::class.java)
        return confgHud
    }
}