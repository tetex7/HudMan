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

import com.trs.hudman.gui.hudmods.AbstractHudElement;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ElementRegistry
{
    private final HashMap<ResourceLocation, NewAbstractHudElementHandler> elementMap = new HashMap<>();


    public void register(NamespacePath namespacePath, NewAbstractHudElementHandler newElementHandler)
    {
        elementMap.put(namespacePath.getResourceLocation(), newElementHandler);
    }

    public void register(Map<NamespacePath, NewAbstractHudElementHandler> elementsMap)
    {
        for (Map.Entry<NamespacePath, NewAbstractHudElementHandler> entry : elementsMap.entrySet())
        {
             this.elementMap.put(entry.getKey().getResourceLocation(), entry.getValue());
        }
    }

    public boolean has(NamespacePath namespacePath)
    {
        return elementMap.containsKey(namespacePath.getResourceLocation());
    }

    public NewAbstractHudElementHandler get(NamespacePath namespacePath)
    {
        return elementMap.get(namespacePath.getResourceLocation());
    }

    public final Map<ResourceLocation, NewAbstractHudElementHandler> getElementMap()
    {
        return elementMap;
    }
}
