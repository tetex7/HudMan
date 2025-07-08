/*
 * Copyright (C) 2025  Tetex7
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
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;

public final class FastRegistrar
{
    /**
     *
     * @param namespace Top level namespace
     * @param packagePath Path to a package containing HUD elements
     */
    @SuppressWarnings("unchecked")
    public static void searchForRegistrables(
            @Pattern(NamespacePath.ALLOWED_CHAR_REGEX) String namespace,
            @Language(value = "JAVA", prefix = "import ", suffix = ".*;") String packagePath
    ) {
        try
        {
            List<Class<?>> classes = getClasses(packagePath);
            for (Class<?> clazz : classes)
            {
                RegistrableHudElement registrableHudElement = clazz.getAnnotation(RegistrableHudElement.class);
                if (registrableHudElement == null) continue;
                if (AbstractHudElement.class.isAssignableFrom(clazz)) // Checking if inherited from the AbstractHudElement class
                {
                    try
                    {
                        Constructor<AbstractHudElement> constructor = (Constructor<AbstractHudElement>) clazz.getConstructor(
                                AbstractHudElement.class, //root Mostly time it's null and will probably be removed
                                Minecraft.class, //client The current Minecraft client
                                Vec2i.class, //cords The coordinates of the element on the user screen
                                JsonConfigHudElement.class //jsonElement The Jason config structure turned into a Java class
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
            if (e instanceof ReportedException reportedException)
            {
                throw reportedException; // Ooh, scary but this was done on purpose to allow crashes to occur
            }

            HudState.LOGGER.error("Exception on Registering HudElement class's in Package:'{}' for namespace {}\n{}",
                    packagePath,
                    namespace,
                    ConfigHelper.stackTraceString(e)
            );
        }
    }

    /**
     * Fixed in regards with known_bugs.txt(B02)
     */
    private static List<Class<?>> getClasses(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        var classLoader = Thread.currentThread().getContextClassLoader();
        var resource = classLoader.getResource(path);

        if (resource == null) {
            throw new RuntimeException("Package not found: " + packageName);
        }

        if (resource.getProtocol().equals("jar")) {
            String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
            try (var jarFile = new java.util.jar.JarFile(jarPath)) {
                var entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    var entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(path) && name.endsWith(".class") && !name.contains("$")) {
                        String className = name.replace('/', '.').substring(0, name.length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
        } else {
            File directory = new File(resource.toURI());
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }
}
