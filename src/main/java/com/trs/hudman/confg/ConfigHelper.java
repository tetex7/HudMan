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

package com.trs.hudman.confg;

import com.trs.hudman.gui.hudmods.*;
import com.trs.hudman.util.CrashElement;
import com.trs.hudman.util.NamespacePath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import com.trs.hudman.HudState;

@Environment(EnvType.CLIENT)
public final class ConfigHelper
{
    @Internal
    public static void mkHud(Minecraft minecraft)
    {
        HudState.hudElements.clear();
        for (JsonConfigHudElement element : HudState.getConfig().elements())
        {
            if (element.enable())
            {
                NamespacePath path = element.elementId();

                if (path.getNamespace().equals(NamespacePath.MOD_NAMESPACE) || path.getNamespace().equals(NamespacePath.MINECRAFT_NAMESPACE))
                {
                    HudState.LOGGER.info("New ElementName:'{}' to load on built-in Namespace:'{}'", path.getPath(), path.getNamespace());
                    try
                    {
                        if (HudState.elementRegistry.hasElement(path))
                        {
                            HudState.hudElements.push(HudState.elementRegistry.get(path).create(null, minecraft, element.cords(), element));
                            HudState.LOGGER.info("loaded ElementName:'{}' on built-in Namespace:'{}'", path.getPath(), path.getNamespace());
                        }
                        else
                        {
                            HudState.LOGGER.info("no Element by ElementName:'{}' on built-in Namespace:'{}'", path.getPath(), path.getNamespace());
                            if (HudState.getErrorNotification())
                            {
                                showToast("Failure on Element Load", "no Element by ElementPath:'" + path.getFullPath() + '\'');
                            }
                        }
                    }
                    catch (Throwable exception)
                    {
                        HudState.LOGGER.error("Exception in built-in Namespace:'{}' on Loading ElementName:'{}'\n{}", path.getNamespace(), path.getPath(), stackTraceString(exception));
                        if (HudState.getErrorNotification())
                        {
                            showToast("Failure on Element Load", "Exception on load '" + path.getFullPath() + '\'');
                        }
                    }
                }
                else
                {
                    HudState.LOGGER.info("New ElementName:'{}' to load on External Namespace:'{}'", path.getPath(), path.getNamespace());
                    try
                    {
                        if (HudState.elementRegistry.hasElement(path))
                        {
                            HudState.hudElements.push(HudState.elementRegistry.get(path).create(null, minecraft, element.cords(), element));
                            HudState.LOGGER.info("loaded ElementName:'{}' on External Namespace:'{}'", path.getPath(), path.getNamespace());
                        }
                        else
                        {
                            HudState.LOGGER.error("no Element by ElementName:'{}' on External Namespace:'{}'", path.getPath(), path.getNamespace());
                            if (HudState.getErrorNotification())
                            {
                                showToast("Failure on Element Load", "no Element by ElementPath:'" + path.getFullPath() + '\'');
                            }
                        }

                    }
                    catch (Throwable exception)
                    {
                        HudState.LOGGER.error("Exception in External Namespace:'{}' on Loading ElementName:'{}'\n{}", path.getNamespace(), path.getPath(), stackTraceString(exception));
                        if (HudState.getErrorNotification())
                        {
                            showToast("Failure on Element Load", "Exception on load '" + path.getFullPath() + '\'');
                        }
                    }
                }
            }
        }
    }

    public static void registerAll()
    {
        HudState.elementRegistry.register(
                Map.of(
                        NamespacePath.pathOf("cords"), CordsElement::new,
                        NamespacePath.pathOf("text"), TextElement::new,
                        NamespacePath.pathOf("compass"), CompassElement::new,
                        NamespacePath.pathOf("velocity_vector"), VelocityVectorElement::new,
                        NamespacePath.pathOf("fps"), FPSElement::new
                )
        );

        HudState.LOGGER.info("Registered all HudElement");
    }

    private static void randerErrorMsg(String text, Minecraft client)
    {
        ToastComponent toastComponent = Minecraft.getInstance().getToasts();
        SystemToast.multiline(
                client,
                SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                Component.literal("\"OH NO\" Error on HudElement Load"),
                Component.literal(text)
        );
    }

    public static String stackTraceString(Throwable throwable)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        throwable.printStackTrace(pw);

        pw.flush();
        return sw.toString();
    }

    public static void osWarn()
    {
        if (Util.getPlatform() == Util.OS.LINUX)
        {
            showToast("Linux warning", "Linux is untested But is supported");
        }
        else if (Util.getPlatform() == Util.OS.OSX)
        {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F, 1.0F));
            showToast("No support for Mac OS", "I do not own a Mac nor care to test for Mac OS");
        }
        else if (Util.getPlatform() == Util.OS.SOLARIS)
        {
            showToast("Have fun with spark", "Solaris Haven't heard that name in about two decades");
        }
        else if (Util.getPlatform() == Util.OS.UNKNOWN)
        {
            showToast("How did this even happen", "This is a unknown OS");
        }
    }

    public static void showToast(String title, String message) {
        Minecraft minecraft = Minecraft.getInstance();

        // Create the title and message components with optional formatting
        Component titleComponent = Component.literal(title).withStyle(ChatFormatting.BOLD);
        Component messageComponent = Component.literal(message).withStyle(ChatFormatting.ITALIC, ChatFormatting.GOLD);
        // Create and add the toast to the toast manager
        minecraft.getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.PACK_LOAD_FAILURE, titleComponent, messageComponent));
    }
}
