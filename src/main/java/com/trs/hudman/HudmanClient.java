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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Environment(EnvType.CLIENT)
public class HudmanClient implements ClientModInitializer
{

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

    private static final KeyMapping HUDMAN_DEBUG_MENU;

    static
    {
        if (HudState.jarDebug)
        {
            HUDMAN_DEBUG_MENU = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                    "key.hudman.huddebug",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_F10,
                    "key.categories.hudman"
            ));
        }
        else
        {
            HUDMAN_DEBUG_MENU = null;
        }
    }

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

            if (HUDMAN_RESET.consumeClick())
            {
                if (HudState.showHud)
                {
                    HudResetEvent.call();
                }
            }

            if (HUDMAN_SHOW.consumeClick())
            {
                HudState.hudElements.clear();
                HudState.showHud = !HudState.showHud;
                if (HudState.showHud)
                {
                    HudResetEvent.call();
                }
            }

            if (HUDMAN_DEBUG_MENU != null)
            {
                if (HUDMAN_DEBUG_MENU.consumeClick())
                {
                    HudState.LOGGER.info("WIP");
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
                    List.of(
                            new JsonConfigHudElement(
                                    NamespacePath.of("hudman:velocity_vector"),
                                    new Vec2i(100, 110),
                                    0,
                                    0,
                                    0.75f,
                                    "",
                                    true,
                                    List.of(
                                            "doTooltip"
                                    )
                            )
                    ),
                    false,
                    true
            );
            HudState.LOGGER.info(conf.toString());
            try (FileWriter writer = new FileWriter(conf, StandardCharsets.UTF_8))
            {
                writer.write(prettyPrintWithIndent(new GsonBuilder().registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathAdapter()).create().toJson(congHud), 4));
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

    private static String prettyPrintWithIndent(String json, int indentSize)
    {
        JsonElement jsonElement = JsonParser.parseString(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Step 1: Convert JSON to a pretty-printed version with default indentation
        String prettyJson = gson.toJson(jsonElement);

        // Step 2: Define the custom indentation string
        String indent = " ".repeat(indentSize);

        // Step 3: Use StringBuilder to construct the final string with custom indentation
        StringBuilder indentedJson = new StringBuilder();
        int currentIndentLevel = 0;

        // Split the JSON into lines and apply custom indentation per line
        for (String line : prettyJson.split("\n")) {
            String trimmedLine = line.trim();

            // Decrease indent level for closing braces/brackets
            if (trimmedLine.startsWith("}") || trimmedLine.startsWith("]")) {
                currentIndentLevel--;
            }

            // Apply current indentation and add line
            indentedJson.append(indent.repeat(currentIndentLevel)).append(trimmedLine).append("\n");

            // Increase indent level after opening braces/brackets
            if (trimmedLine.endsWith("{") || trimmedLine.endsWith("[")) {
                currentIndentLevel++;
            }
        }

        return indentedJson.toString().trim();
    }
}

