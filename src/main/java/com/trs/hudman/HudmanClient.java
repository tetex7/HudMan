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
import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.confg.JsonConfigHudFile;
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
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;

@Environment(EnvType.CLIENT)
public class HudmanClient implements ClientModInitializer
{

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
            "key.categories.hudman"
    ));

    private static final KeyMapping HUDMAN_SHOW = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.hudman.hudshow",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F8,
            "key.categories.hudman"
    ));

    private static ClientLevel lastWorld = null;
    private static boolean worldGood = false;

    @Override
    public void onInitializeClient()
    {
        ConfigHelper.registerAll();
        HudResetEvent.EVENT.register(() -> {
            ConfigHelper.mkHud(Minecraft.getInstance());
            return true;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (HUDMAN_RESET.consumeClick())
            {
                if (HudState.showHud)
                {
                    HudResetEvent.call();
                }
            }

            while (HUDMAN_SHOW.consumeClick())
            {
                HudState.hudElements.clear();
                HudState.showHud = !HudState.showHud;
                if (HudState.showHud)
                {
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

            HudState.LOGGER.info("running World load event");
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
            HudState.LOGGER.info("running World unload event");
            worldGood = false;
            HudState.hudElements.clear();
        });

        if (!NamespacePath.pathOf("test").equals(NamespacePath.pathOf("test")))
        {
            throw new IllegalStateException("Error: 'NamespacePath::equals' function failed self test");
        }

        File conf = new File(HudState.configPath);
        if (!conf.exists())
        {
            JsonConfigHudFile congHud = new JsonConfigHudFile(
                    "v1.0",
                    new JsonConfigHudElement[]{
                            new JsonConfigHudElement(
                                    "null",
                                    new Vec2i(0, 0),
                                    0,
                                    0,
                                    1f,
                                    "",
                                    false,
                                    new String[]{""}
                            ),

                    },
                    false,
                    true
            );
            HudState.LOGGER.info(conf.toString());
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
        /*File preset_confg_dir = new File(HudState.configDirPath + "/hudman_conf");
        if (!preset_confg_dir.exists())
        {
            if (!preset_confg_dir.mkdir())
            {
                CrashReport crashReport = CrashReport.forThrowable(new RuntimeException("Fell to make directory " + preset_confg_dir), "Fell to make directory");
                CrashReportCategory category = crashReport.addCategory("File I/O");
                category.setDetail("Config File Path", preset_confg_dir.getAbsolutePath());
                throw new ReportedException(crashReport);
            }

            try (FileWriter writer = new FileWriter(HudState.configDirPath + "/hudman/placeholder.txt", StandardCharsets.US_ASCII))
            {
                writer.write("This folder is work in progress");
            }
            catch (IOException e)
            {
                CrashReport crashReport = CrashReport.forThrowable(e, "Failed to write a Placeholder file");
                CrashReportCategory category = crashReport.addCategory("File I/O");
                category.setDetail("Config File Path", preset_confg_dir.getAbsolutePath());
                throw new ReportedException(crashReport);
            }
        }*/
    }
}

