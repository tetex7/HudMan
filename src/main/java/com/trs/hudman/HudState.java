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

import com.google.gson.GsonBuilder;
import com.trs.hudman.confg.ConfigHelper;
import com.trs.hudman.confg.JsonConfigHudFile;
import com.trs.hudman.util.NamespacePath;
import net.minecraft.client.Minecraft;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.trs.hudman.events.HudResetEvent;
import com.trs.hudman.gui.hudmods.AbstractHudElement;
import com.trs.hudman.util.ElementRegistry;
import com.trs.hudman.util.INamespaceHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class HudState
{

    public static final String MODID = "hudman";

    public static final Stack<AbstractHudElement> hudElements = new Stack<>();

    @Internal
    public static final Logger LOGGER = LoggerFactory.getLogger("Hudman");

    @Internal
    public static boolean showHud = true;

    private static boolean configDebug = false;

    public static final Map<String, ResourceLocation> gameHudElements = Map.of(
            "hotbar", new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "hotbar"),
            "effectbar", new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "effectbar"),
            "healthbar", new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "healthbar")
    );

    public static final ElementRegistry elementRegistry = new ElementRegistry();

    public static final String configPath = Minecraft.getInstance().gameDirectory.toString() + "/config/hudman.json";

    public static final String configDirPath = Minecraft.getInstance().gameDirectory.toString() + "/config";

    @Deprecated(forRemoval = true, since = "1.0")
    public static final HashMap<String, INamespaceHandler> namespaceHandlers = new HashMap<>();

    private static boolean errorNotification = true;

    static {
        HudResetEvent.EVENT.register((client) -> {
            JsonConfigHudFile jconfig = getConfig();
            LOGGER.info("config_debug is " + jconfig.debug());
            configDebug = jconfig.debug();
            errorNotification = jconfig.errorNotification();
            return true;
        });

        /*try
        {
            var bytes = Objects.requireNonNull(NamespacePath.class.getClassLoader().getResourceAsStream("hudman.debug.flag")).readAllBytes();
            if (bytes.length == 1)
            {
                jarDebug = bytes[0] == 0x01;
                LOGGER.info("DEBUG mode active");
            }
            else
            {
                jarDebug = false;
            }
        } catch (Throwable e)
        {
            throw new RuntimeException(e);
        }*/
    }

    private HudState()
    {
        throw new RuntimeException("WTF");
    }

    public static boolean getConfigDebug() {
        return configDebug;
    }

    public static boolean getErrorNotification() {
        return errorNotification;
    }

    public static JsonConfigHudFile getConfig()
    {
        try
        {
            var gson = new GsonBuilder()
                    .registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathAdapter())
                    /*.registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathJsonDeserializer())
                    .registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathJsonSerializer())*/
                    .create();
            String json = Files.readString(Paths.get(configPath), StandardCharsets.UTF_8);
            return gson.fromJson(json, JsonConfigHudFile.class);
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to load config from {}\n{}", configPath, ConfigHelper.stackTraceString(e));
            throw new RuntimeException("Critical error: Could not load configuration file at " + configPath, e);
        }
    }
}

