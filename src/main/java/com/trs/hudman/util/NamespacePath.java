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

package com.trs.hudman.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import com.trs.hudman.HudState;

/**
 * a wrapper for the minecraft {@link ResourceLocation}
 */
@Environment(EnvType.CLIENT)
public class NamespacePath implements Comparable<NamespacePath>
{
    public static final String MOD_NAMESPACE = HudState.MODID;
    public static final String MINECRAFT_NAMESPACE = ResourceLocation.DEFAULT_NAMESPACE;

    private final String fullPath;
    private final ResourceLocation resourceLocation;
    private final String namespace;
    private final String path;

    public static @NotNull NamespacePath pathMcOf(String path)
    {
        return new NamespacePath(MINECRAFT_NAMESPACE, path);
    }

    public static @NotNull NamespacePath pathOf(String path)
    {
        return new NamespacePath(MOD_NAMESPACE, path);
    }

    public static @NotNull NamespacePath of(String namespace, String path)
    {
        return new NamespacePath(namespace, path);
    }

    public static @NotNull NamespacePath of(String fullPath)
    {
        return new NamespacePath(fullPath);
    }

    public static @NotNull NamespacePath of(@NotNull ResourceLocation resource)
    {
        return new NamespacePath(resource);
    }

    private NamespacePath(String namespace, String path)
    {
        this(new ResourceLocation(namespace, path));
    }

    private NamespacePath(@NotNull ResourceLocation resource)
    {
        this(resource.toString());
    }

    private NamespacePath(String fullPath)
    {
        this.fullPath = fullPath;
        this.resourceLocation = new ResourceLocation(this.fullPath);
        this.namespace = this.resourceLocation.getNamespace();
        this.path = this.resourceLocation.getPath();
    }

    public final String getNamespace()
    {
        return namespace;
    }

    public final String getFullPath()
    {
        return fullPath;
    }

    public final ResourceLocation getResourceLocation()
    {
        return resourceLocation;
    }

    public final String getPath()
    {
        return path;
    }

    @Override
    public String toString()
    {
        return resourceLocation.toString();
    }

    @Override
    public int compareTo(@NotNull NamespacePath path)
    {
        return this.resourceLocation.compareTo(path.resourceLocation);
    }

    public int compareTo(@NotNull ResourceLocation location)
    {
        return this.resourceLocation.compareTo(location);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof ResourceLocation obj_path)
        {
            return this.resourceLocation.equals(obj_path);
        }
        else if (object instanceof String obj_string)
        {
            return this.resourceLocation.toString().equals(obj_string);
        }
        else if (object instanceof NamespacePath obj_path)
        {
            return this.resourceLocation.equals(obj_path.resourceLocation);
        }
        return super.equals(object);
    }

    @Override
    public int hashCode()
    {
        return this.resourceLocation.hashCode();
    }
}
