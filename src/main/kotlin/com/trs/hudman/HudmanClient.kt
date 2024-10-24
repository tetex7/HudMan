package com.trs.hudman

import com.google.gson.Gson
import com.mojang.blaze3d.platform.InputConstants
import com.trs.hudman.HudState
import com.trs.hudman.confg.ConfgHelper
import com.trs.hudman.confg.JsonConfgHudElement
import com.trs.hudman.confg.JsonConfgHudFile
import com.trs.hudman.events.ClientWorldEvent
import com.trs.hudman.events.HudResetEvent
import com.trs.hudman.gui.screens.HudEditerScreen
import io.github.cottonmc.cotton.gui.widget.data.Vec2i
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.mixin.event.lifecycle.client.ClientWorldMixin
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.network.chat.Component
import org.lwjgl.glfw.GLFW
import java.io.File

@Environment(EnvType.CLIENT)
object HudmanClient : ClientModInitializer
{
    /*var HUDMAN_MAPPING = KeyBindingHelper.registerKeyBinding(KeyMapping(
        "key.hudman.hudediter",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_F9,
        KeyMapping.CATEGORY_MISC
    ))*/

    var HUDMAN_RESET = KeyBindingHelper.registerKeyBinding(KeyMapping(
        "key.hudman.hudreset",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_F12,
        KeyMapping.CATEGORY_MISC
    ))

    var HUDMAN_SHOW = KeyBindingHelper.registerKeyBinding(KeyMapping(
        "key.hudman.hudshow",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_F8,
        KeyMapping.CATEGORY_MISC
    ))

    private var lastWorld: ClientLevel? = null;

    private var waled_good = false

    override fun onInitializeClient()
    {

        HudResetEvent.EVENT.register RET@{
            ConfgHelper.mkHud(Minecraft.getInstance())
            return@RET true
        }

        ClientTickEvents.END_CLIENT_TICK.register {
            /*while(HUDMAN_MAPPING.consumeClick())
            {
                Minecraft.getInstance().setScreen(HudEditerScreen(Component.literal("hud")));
            }*/
            while(HUDMAN_RESET.consumeClick())
            {
                if (HudState.showHud)
                {
                    ConfgHelper.mkHud(Minecraft.getInstance())
                }
            }
            while(HUDMAN_SHOW.consumeClick())
            {
                HudState.hudElements.clear()
                HudState.showHud = !HudState.showHud
                if (HudState.showHud)
                {
                    HudResetEvent.call()
                }
            }

            val currentWorld = Minecraft.getInstance().level

            // Detect if a world has been loaded
            if (currentWorld != null && lastWorld == null) {
                ClientWorldEvent.CLIENT_WORLD_LOAD_EVENT.invoker().interact()
            }

            // Detect if the world has been unloaded
            if (currentWorld == null && lastWorld != null) {
                ClientWorldEvent.CLIENT_WORLD_UNLOAD_EVENT.invoker().interact()
            }

            lastWorld = currentWorld
        }

        ClientWorldEvent.CLIENT_WORLD_LOAD_EVENT.register RET@{
            HudState.LOGGER.info("running World load event")
            if (!waled_good)
            {
                if (Minecraft.getInstance().player == null)
                {
                    return@RET
                }
                HudResetEvent.call()
                waled_good = true
            }
        }

        ClientWorldEvent.CLIENT_WORLD_UNLOAD_EVENT.register {
            HudState.LOGGER.info("running World unload event")
            waled_good = false;
            HudState.hudElements.clear()
        }

        val conf = File("${Minecraft.getInstance().gameDirectory}/hud.json")
        if (!conf.exists())
        {
            val congHud = JsonConfgHudFile(
                "v1.0",
                arrayOf(
                    JsonConfgHudElement(
                        "null",
                        Vec2i(0, 0),
                        0,
                        0,
                        "",
                        true,
                        arrayOf("")
                    )
                )
            )
            HudState.LOGGER.info(conf.toString())
            conf.writer(Charsets.US_ASCII).append(Gson().toJson(congHud)).close()
        }
    }
}
