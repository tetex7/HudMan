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
import com.trs.hudman.mixin.PlayerGuiMixinAccessor;
import com.trs.hudman.util.INamespaceHandler;
import com.trs.hudman.util.NamespacePath;
import com.trs.hudman.util.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
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
        JsonConfgHudFile huds = HudState.getCong();
        HudState.getHudElements().clear();
        for (JsonConfgHudElement element : huds.Elements())
        {
            if (element.enable())
            {
                NamespacePath path = NamespacePath.of(element.elementId());
                HudState.getLOGGER().info("New ElementID:'{}' to load", path);
                if (path.getNamespace().equals("hudman"))
                {
                    try
                    {
                        if (HudState.getElementRegistry().has(path))
                        {
                            HudState.getHudElements().push(HudState.getElementRegistry().get(path).create(null, minecraft, element.cords(), element));
                        }
                        else
                        {
                            HudState.getLOGGER().info("no (new reg system)element by Id:'{}'", element.elementId());


                            /*switch (path.getPath())
                            {
                                case "cords" -> HudState.getHudElements().push(new CordsElement(null, minecraft, element.cords(), element));
                                case "text" -> HudState.getHudElements().push(new TextElement(null, minecraft, element.cords(), element));
                                case "compass" -> HudState.getHudElements().push(new CompassElement(null, minecraft, element.cords(), element));
                                case "velocity_vector" -> HudState.getHudElements().push(new VelocityVectorElement(null, minecraft, element.cords(), element));
                                //case "fps" -> HudState.getHudElements().push(new FPSElement(null, minecraft, element.cords(), element));
                                default ->
                                {
                                    HudState.getLOGGER().info("no element by Id:'{}'", element.elementId());
                                    continue;
                                }
                            }*/
                        }
                        HudState.getLOGGER().info("loaded ElementID:'{}'", path);
                    }
                    catch (Throwable exception)
                    {
                        HudState.getLOGGER().error("Exception in built-in NamespaceHandler:'{}' on Loading ElementName:'{}'\n{}", path.getNamespace(), path.getPath(), stackTraceString(exception));
                    }
                }
                else
                {

                    try
                    {
                        if (HudState.getElementRegistry().has(path))
                        {
                            HudState.getHudElements().push(HudState.getElementRegistry().get(path).create(null, minecraft, element.cords(), element));
                        }
                        else
                        {
                            HudState.getLOGGER().warn("no (new reg system)element by Id:'{}'", element.elementId());
                        }
                        HudState.getLOGGER().info("loaded ElementID:'{}'", path);
                    }
                    catch (Throwable exception)
                    {
                        HudState.getLOGGER().error("Exception in NamespaceHandler:'{}' on Loading ElementName:'{}'\n{}", path.getNamespace(), path.getPath(), stackTraceString(exception));
                    }
                    /*INamespaceHandler name_fun = HudState.getNamespaceHandlers().get(path.getNamespace());
                    if (name_fun != null)
                    {
                        try
                        {
                            boolean ret_v = name_fun.work(element);
                            if (ret_v)
                            {
                                HudState.getLOGGER().warn("NamespaceHandler for Namespace:'{}' returned a bool of 1", path.getNamespace());
                            }
                        }
                        catch (Throwable exception)
                        {
                            HudState.getLOGGER().error("Exception in NamespaceHandler:'{}' on Loading ElementName:'{}'\n{}", path.getNamespace(), path.getPath(), stackTraceString(exception));
                        }
                    }
                    else
                    {
                        HudState.getLOGGER().warn("No NamespaceHandler for ElementID:'{}'", path.getFullPath());
                    }*/
                    continue;
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
