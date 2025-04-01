/*
 * Copyright (C) 2025  Tete
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
import com.google.gson.Strictness;
import com.trs.hudman.confg.ConfigHelper;
import com.trs.hudman.confg.JsonConfigHudFile;
import com.trs.hudman.confg.JsonConfigHudPreset;
import com.trs.hudman.confg.JsonConfigHudPresetsDefinitionsFile;
import com.trs.hudman.util.NamespacePath;
import net.minecraft.client.Minecraft;
import org.slf4j.LoggerFactory;

import java.io.*;
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

/**
 * an essential class for this mod holding state flags
 * And essential data
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class HudState
{

    public static final String MODID = "hudman";

    public static final Stack<AbstractHudElement> hudElements = new Stack<>();

    @Internal
    public static final Logger LOGGER = LoggerFactory.getLogger("Hudman");

    public static final HashMap<NamespacePath, JsonConfigHudPreset> hudPresetMap = new HashMap<>();

    @Internal
    public static boolean showHud = true;

    private static boolean configDebug = false;

    public static final Map<String, ResourceLocation> gameHudElements = Map.of(
            "hotbar", ResourceLocation.fromNamespaceAndPath(ResourceLocation.DEFAULT_NAMESPACE, "hotbar"),
            "effectbar", ResourceLocation.fromNamespaceAndPath(ResourceLocation.DEFAULT_NAMESPACE, "effectbar"),
            "healthbar", ResourceLocation.fromNamespaceAndPath(ResourceLocation.DEFAULT_NAMESPACE, "healthbar")
    );

    public static final ElementRegistry elementRegistry = new ElementRegistry();

    public static final String configDirPath = Minecraft.getInstance().gameDirectory.toString() + "/config";

    public static final String configPath = configDirPath + "/hudman.json";

    public static final String presetDefinitionsPath = configDirPath + "/hudmanPresets.json";

    public static final String presetDirPath = configDirPath + "/HudmanPresets";

    @Deprecated
    public static final HashMap<String, INamespaceHandler> namespaceHandlers = new HashMap<>();

    private static boolean errorNotification = true;

    private static List<NamespacePath> readPresetsDefinitions()
    {
        try
        {
            var gson = new GsonBuilder()
                    .registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathAdapter())
                    .setStrictness(Strictness.LENIENT)
                    /*.registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathJsonDeserializer())
                    .registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathJsonSerializer())*/
                    .create();
            String json = Files.readString(Paths.get(presetDefinitionsPath), StandardCharsets.UTF_8);
            return gson.fromJson(json, JsonConfigHudPresetsDefinitionsFile.class).hudPreset();
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to load preset config from {}\n{}", presetDefinitionsPath, ConfigHelper.stackTraceString(e));
            throw new RuntimeException("Critical error: Could not load preset configuration file at " + presetDefinitionsPath, e);
        }
    }

    private static JsonConfigHudPreset readPresetJson(String path) throws FileNotFoundException
    {
        if (!new File(path).exists())
        {
            throw new FileNotFoundException(path);
        }
        try
        {
            var gson = new GsonBuilder()
                    .registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathAdapter())
                    .setStrictness(Strictness.LENIENT)
                    /*.registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathJsonDeserializer())
                    .registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathJsonSerializer())*/
                    .create();
            String json = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
            return gson.fromJson(json, JsonConfigHudPreset.class);
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to load preset config from {}\n{}", path, ConfigHelper.stackTraceString(e));
            throw new RuntimeException("Critical error: Could not load preset configuration file at " + path, e);
        }
    }

    private static void setUpPresetConfigSystem()
    {
        hudPresetMap.clear();
        if (!new File(HudState.presetDefinitionsPath).exists()) return;
        List<NamespacePath> presets = readPresetsDefinitions();

        for (final NamespacePath presetPath : presets)
        {
            final String path = presetDirPath + '/' + presetPath.getNamespace() + '/' + presetPath.getPath() + ".json";
            if (presetPath.getNamespace().equals(NamespacePath.MINECRAFT_NAMESPACE) || presetPath.getNamespace().equals(NamespacePath.MOD_NAMESPACE))
            {
                throw new RuntimeException("You cannot use hudman namespace or minecraft's namespace for a preset");
            }
            try
            {

                hudPresetMap.put(presetPath, readPresetJson(path));
                LOGGER.info(
                        "loaded preset def namespacePath:'{}' file:'{}'",
                        presetPath,
                        presetPath.getNamespace() + '/' + presetPath.getPath() + ".json"
                );
            }
            catch (FileNotFoundException e)
            {
                LOGGER.error("{} Not Found", path);
            }
        }
        loadPresetFormPack();
    }

    public static String getStringFromReader(BufferedReader reader) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null)
        {
            stringBuilder.append(line).append('\n');
        }
        return stringBuilder.toString();
    }

    private static void loadPresetFormPack()
    {
        Map<ResourceLocation, net.minecraft.server.packs.resources.Resource> resData = Minecraft.getInstance().getResourceManager().listResources("hudman_presets", (resourceLocation)->
        {
            final NamespacePath namespacePath = NamespacePath.of(resourceLocation);
            if (namespacePath.getPath().endsWith(".json"))
            {
                return true;
            }
            return false;
        });
        try
        {
            if (resData.isEmpty()) return;
            for (final Map.Entry<ResourceLocation, net.minecraft.server.packs.resources.Resource> resPreset : resData.entrySet())
            {
                final NamespacePath namespacePath = NamespacePath.of(resPreset.getKey());
                var gson = new GsonBuilder()
                        .registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathAdapter())
                        .setStrictness(Strictness.LENIENT)
                        /*.registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathJsonDeserializer())
                        .registerTypeAdapter(NamespacePath.class, new NamespacePath.NamespacePathJsonSerializer())*/
                        .create();
                JsonConfigHudPreset jhudPreset = gson.fromJson(
                        getStringFromReader(resPreset.getValue().openAsReader()),
                        JsonConfigHudPreset.class
                );

                var presetPath = NamespacePath.of(namespacePath.getNamespace(), Paths.get(namespacePath.getPath())
                        .getFileName()
                        .toString()
                        .replaceFirst("[.][^.]+$", ""));
                hudPresetMap.put(
                        presetPath,
                        jhudPreset
                );
                LOGGER.info(
                        "loaded preset form pack namespacePath:'{}' Pack location:'{}'",
                        presetPath,
                        resPreset.getKey().toString()
                );
            }
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    static {
        HudResetEvent.EVENT.register(() -> {
            JsonConfigHudFile jconfig = getConfig();
            LOGGER.info("config_debug is " + jconfig.debug());
            configDebug = jconfig.debug();
            errorNotification = jconfig.errorNotification();
            setUpPresetConfigSystem();
            return true;
        });
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

    public static String fastCat(Object... objs)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (final Object obj : objs)
        {
            stringBuilder.append(obj.toString());
        }
        return stringBuilder.toString();
    }
}

