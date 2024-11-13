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

import com.trs.hudman.HudState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ElementRegistry
{
    private final HashMap<@NotNull NamespacePath, @NotNull NewAbstractHudElementHandler> elementMap = new HashMap<>();

    public void register(@NotNull NamespacePath namespacePath, NewAbstractHudElementHandler newElementHandler)
    {
        elementMap.put(namespacePath, newElementHandler);
    }

    public void register(@NotNull Map<NamespacePath, NewAbstractHudElementHandler> elementsMap)
    {
        this.elementMap.putAll(elementsMap);
    }

    public void unregister(@NotNull NamespacePath namespacePath)
    {
        if (this.hasElement(namespacePath))
        {
            elementMap.remove(namespacePath);
        }
        else
        {
            throw new NoSuchElementException(String.format("No Registered Element by Path: %s", namespacePath.getFullPath()));
        }
    }

    public boolean hasElement(@NotNull NamespacePath namespacePath)
    {
        return elementMap.containsKey(namespacePath);
    }

    public @NotNull NewAbstractHudElementHandler get(@NotNull NamespacePath namespacePath)
    {
        if (this.hasElement(namespacePath))
        {
            return elementMap.get(namespacePath);
        }
        else
        {
            throw new NoSuchElementException(String.format("No Registered Element by Path: %s", namespacePath.getFullPath()));
        }
    }

    public final @NotNull Map<NamespacePath, NewAbstractHudElementHandler> getElementMap()
    {
        return elementMap;
    }
}
