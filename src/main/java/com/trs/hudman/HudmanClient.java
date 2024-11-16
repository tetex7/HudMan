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

package com.trs.hudman;

import com.google.gson.Gson;
import com.mojang.blaze3d.platform.InputConstants;
import com.trs.hudman.confg.ConfigHelper;
import com.trs.hudman.confg.JsonConfgHudElement;
import com.trs.hudman.confg.JsonConfgHudFile;
import com.trs.hudman.events.ClientWorldEvent;
import com.trs.hudman.events.HudResetEvent;
import com.trs.hudman.util.NamespacePath;
import com.trs.hudman.util.Vec2i;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.trs.hudman.HudState;

@Environment(EnvType.CLIENT)
public class HudmanClient implements ClientModInitializer {

    /* Uncomment to register HUD editor keybinding
    private static final KeyMapping HUDMAN_MAPPING = KeyBindingHelper.registerKeyBinding(new KeyMapping(
        "key.hudman.hudediter",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_F9,
        KeyMapping.CATEGORY_MISC
    ));
    */

    private static final KeyMapping HUDMAN_RESET = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.hudman.hudreset",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F12,
            KeyMapping.CATEGORY_MISC
    ));

    private static final KeyMapping HUDMAN_SHOW = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.hudman.hudshow",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F8,
            KeyMapping.CATEGORY_MISC
    ));

    private static ClientLevel lastWorld = null;
    private static boolean worldGood = false;

    @Override
    public void onInitializeClient()
    {
        HudResetEvent.EVENT.register(() -> {
            ConfigHelper.mkHud(Minecraft.getInstance());
            return true;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (HUDMAN_RESET.consumeClick())
            {
                if (HudState.getShowHud())
                {
                    HudResetEvent.call();
                }
            }

            while (HUDMAN_SHOW.consumeClick())
            {
                HudState.getHudElements().clear();
                HudState.setShowHud(!HudState.getShowHud());
                if (HudState.getShowHud()) {
                    HudResetEvent.call();
                }
            }

            ClientLevel currentWorld = Minecraft.getInstance().level;

            // Detect if a world has been loaded
            if (currentWorld != null && lastWorld == null)
            {
                ClientWorldEvent.CLIENT_WORLD_LOAD_EVENT.invoker().interact();
            }

            // Detect if the world has been unloaded
            if (currentWorld == null && lastWorld != null)
            {
                ClientWorldEvent.CLIENT_WORLD_UNLOAD_EVENT.invoker().interact();
            }

            lastWorld = currentWorld;
        });

        ClientWorldEvent.CLIENT_WORLD_LOAD_EVENT.register(() -> {
            HudState.getLOGGER().info("running World load event");
            if (!worldGood)
            {
                if (Minecraft.getInstance().player == null)
                {
                    return;
                }
                HudResetEvent.call();
                worldGood = true;
            }
        });

        ClientWorldEvent.CLIENT_WORLD_UNLOAD_EVENT.register(() -> {
            HudState.getLOGGER().info("running World unload event");
            worldGood = false;
            HudState.getHudElements().clear();
        });

        if (!NamespacePath.pathOf("test").equals(NamespacePath.pathOf("test")))
        {
            throw new RuntimeException("Error: 'NamespacePath::equals' function failed self test ");
        }

        File conf = new File(HudState.getConfigPath());
        if (!conf.exists())
        {
            JsonConfgHudFile congHud = new JsonConfgHudFile(
                    "v1.0",
                    new JsonConfgHudElement[]{
                            new JsonConfgHudElement(
                                    "null",
                                    new Vec2i(0, 0),
                                    0,
                                    0,
                                    1f,
                                    "",
                                    false,
                                    new String[]{""}
                            )
                    },
                    false
            );
            HudState.getLOGGER().info(conf.toString());
            try (FileWriter writer = new FileWriter(conf, StandardCharsets.US_ASCII))
            {
                writer.write(new Gson().toJson(congHud));
            }
            catch (IOException e)
            {
                CrashReport crashReport = CrashReport.forThrowable(e, "Failed to write config file");
                CrashReportCategory category = crashReport.addCategory("File I/O");
                category.setDetail("Config File Path", conf.getAbsolutePath());
                throw new ReportedException(crashReport);
            }
        }
    }
}

