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

import com.trs.hudman.HudState;
import com.trs.hudman.gui.hudmods.*;
import com.trs.hudman.util.NamespacePath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class ConfigHelper
{
    @Internal
    public static void mkHud(Minecraft minecraft)
    {
        HudState.getHudElements().clear();
        for (JsonConfgHudElement element : HudState.getCong().elements())
        {
            if (element.enable())
            {
                NamespacePath path = NamespacePath.of(element.elementId());

                if (path.getNamespace().equals(NamespacePath.MOD_NAMESPACE) || path.getNamespace().equals(NamespacePath.MINECRAFT_NAMESPACE))
                {
                    HudState.getLOGGER().info("New ElementName:'{}' to load on built-in Namespace:'{}'", path.getPath(), path.getNamespace());
                    try
                    {
                        if (HudState.getElementRegistry().hasElement(path))
                        {
                            HudState.getHudElements().push(HudState.getElementRegistry().get(path).create(null, minecraft, element.cords(), element));
                            HudState.getLOGGER().info("loaded ElementName:'{}' on built-in Namespace:'{}'", path.getPath(), path.getNamespace());
                        }
                        else
                        {
                            HudState.getLOGGER().info("no Element by ElementName:'{}' on built-in Namespace:'{}'", path.getPath(), path.getNamespace());
                        }
                    }
                    catch (Throwable exception)
                    {
                        HudState.getLOGGER().error("Exception in built-in Namespace:'{}' on Loading ElementName:'{}'\n{}", path.getNamespace(), path.getPath(), stackTraceString(exception));
                    }
                }
                else
                {
                    HudState.getLOGGER().info("New ElementName:'{}' to load on External Namespace:'{}'", path.getPath(), path.getNamespace());
                    try
                    {
                        if (HudState.getElementRegistry().hasElement(path))
                        {
                            HudState.getHudElements().push(HudState.getElementRegistry().get(path).create(null, minecraft, element.cords(), element));
                            HudState.getLOGGER().info("loaded ElementName:'{}' on External Namespace:'{}'", path.getPath(), path.getNamespace());
                        }
                        else
                        {
                            HudState.getLOGGER().error("no Element by ElementName:'{}' on External Namespace:'{}'", path.getPath(), path.getNamespace());
                        }

                    }
                    catch (Throwable exception)
                    {
                        HudState.getLOGGER().error("Exception in External Namespace:'{}' on Loading ElementName:'{}'\n{}", path.getNamespace(), path.getPath(), stackTraceString(exception));
                    }
                }
            }
        }
    }

    public static void registerAll()
    {
        HudState.getElementRegistry().register(
                Map.of(
                        NamespacePath.pathOf("cords"), CordsElement::new,
                        NamespacePath.pathOf("text"), TextElement::new,
                        NamespacePath.pathOf("compass"), CompassElement::new,
                        NamespacePath.pathOf("velocity_vector"), VelocityVectorElement::new,
                        NamespacePath.pathOf("fps"), FPSElement::new
                )
        );

        HudState.getLOGGER().info("Registered all HudElement");
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
}
