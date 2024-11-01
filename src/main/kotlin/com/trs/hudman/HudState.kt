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

package com.trs.hudman

import com.google.gson.Gson
import com.trs.hudman.confg.ConfigHelper
import net.minecraft.client.Minecraft
import org.slf4j.LoggerFactory
import java.util.*
import com.trs.hudman.confg.JsonConfgHudFile
import com.trs.hudman.events.HudResetEvent
import com.trs.hudman.gui.hudmods.AbstractHudElement
import com.trs.hudman.util.ElementRegistry
import com.trs.hudman.util.INamespaceHandler
import com.trs.hudman.util.NewAbstractHudElementHandler
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.ApiStatus.Internal
import org.slf4j.Logger
import java.io.File

@Environment(EnvType.CLIENT)
object HudState
{
    @JvmStatic
    val modid = "hudman"

    @JvmStatic
    val hudElements = Stack<AbstractHudElement>()

    @get:Internal
    @JvmStatic
    val LOGGER:Logger = LoggerFactory.getLogger("Hudman")

    @JvmStatic
    @set:Internal
    var showHud:Boolean = true

    @JvmStatic
    var configDebug: Boolean = false
        private set

    @JvmStatic
    val gameHudElements: Map<String, ResourceLocation> = mapOf<String, ResourceLocation>(
        "hotbar" to ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "hotbar"),
        "effectbar" to ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "effectbar"),
    )

    @JvmStatic
    val elementRegistry = ElementRegistry()

    @JvmStatic
    val configPath = "${Minecraft.getInstance().gameDirectory}/config/hudman.json"

    @JvmStatic
    val namespaceHandlers: HashMap<String, INamespaceHandler> = HashMap()

    @JvmStatic
    fun getCong(): JsonConfgHudFile
    {
        val confpath = configPath
        val confgHud: JsonConfgHudFile = Gson().fromJson<JsonConfgHudFile>(File(confpath).readText(Charsets.UTF_8), JsonConfgHudFile::class.java)
        return confgHud
    }

    init
    {
        ConfigHelper.registerAll();
        HudResetEvent.EVENT.register RET@{
            val jconfig = getCong()
            LOGGER.info("config_debug is ${jconfig.bDebug()}")
            configDebug = jconfig.bDebug()
            return@RET true
        }
    }
}