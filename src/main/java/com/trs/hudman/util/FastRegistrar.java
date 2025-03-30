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

package com.trs.hudman.util;

import com.trs.hudman.HudState;
import com.trs.hudman.confg.ConfigHelper;
import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.gui.hudmods.AbstractHudElement;
import com.trs.hudman.util.annotations.RegistrableHudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.ReportedException;
import net.minecraft.CrashReport;

public final class FastRegistrar
{
    @SuppressWarnings("unchecked")
    public static void searchForRegistrables(String namespace, String packagePath)
    {
        try
        {
            List<Class<?>> classes = getClasses(packagePath);
            for (Class<?> clazz : classes)
            {
                RegistrableHudElement registrableHudElement = clazz.getAnnotation(RegistrableHudElement.class);
                if (registrableHudElement == null) continue;
                if (AbstractHudElement.class.isAssignableFrom(clazz))
                {
                    try
                    {
                        Constructor<AbstractHudElement> constructor = (Constructor<AbstractHudElement>) clazz.getConstructor(
                                AbstractHudElement.class,
                                Minecraft.class,
                                Vec2i.class,
                                JsonConfigHudElement.class
                        );
                        HudState.elementRegistry.register(
                                NamespacePath.of(namespace, registrableHudElement.regName()),
                                constructor::newInstance
                        );
                    }
                    catch (Exception e)
                    {
                        throw new ReportedException(
                               CrashReport.forThrowable(e,
                                       String.format("Exception on Registering HudElement class's in Class:'%s' for Namespace:'%s'\n%s",
                                               clazz.getName(),
                                               namespace,
                                               ConfigHelper.stackTraceString(e)
                                       )
                               )
                        );
                    }
                }
            }
        } catch (Exception e)
        {
            if (e instanceof ReportedException)
            {
                throw (ReportedException)e;
            }

            HudState.LOGGER.error("Exception on Registering HudElement class's in Package:'{}' for namespace {}\n{}",
                    packagePath,
                    namespace,
                    ConfigHelper.stackTraceString(e)
            );
        }
    }

    private static List<Class<?>> getClasses(String packageName) throws Exception
    {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

        if (resource == null)
        {
            throw new RuntimeException("Package not found: " + packageName);
        }

        File directory = new File(resource.toURI());
        for (File file : Objects.requireNonNull(directory.listFiles()))
        {
            if (file.getName().endsWith(".class"))
            {
                String className = packageName + "." + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }
}
