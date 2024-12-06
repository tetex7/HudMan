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

import com.trs.hudman.util.exceptions.ImproperNamespaceRegisteredException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import com.trs.hudman.HudState;

public class ElementRegistry
{
    private final HashMap<@NotNull NamespacePath, @NotNull NewAbstractHudElementHandler> elementMap = new HashMap<>();

    private final static String  NAMESPACE_PACKAGE = "com.trs.hudman";


    /**
     * A function to check if an element is being improperly registered to this mods namespace from an external package
     * @param newElementHandler Element constructor To be checked
     * @throws ImproperNamespaceRegisteredException If being registered improperly
     * @since 1.5.7
     */
    private static void checkElementValidation(NewAbstractHudElementHandler newElementHandler)
    {
        final var pack = newElementHandler.getClass().getPackageName();
        if (!pack.startsWith(NAMESPACE_PACKAGE))
        {
            throw new ImproperNamespaceRegisteredException(String.format("Improperly registered to namespace:'%s', from External package:'%s'", NamespacePath.MOD_NAMESPACE, pack));
        }
    }

    /**
     * To register an element to be rendered to the mod user HUD
     * @param namespacePath The namespace path for your mod e.g. {@code 'example:compass'}
     * @param newElementHandler A reference to the constructor of your element
     * @throws ImproperNamespaceRegisteredException Exception is thrown if you try to register an element to an improper namespace e.g. hudman-namespace
     */
    public void register(@NotNull NamespacePath namespacePath, NewAbstractHudElementHandler newElementHandler)
    {
        if (namespacePath.getNamespace().equals(NamespacePath.MOD_NAMESPACE))
        {
            checkElementValidation(newElementHandler);
        }
        elementMap.put(namespacePath, newElementHandler);
        HudState.LOGGER.info("Registered HudElement:'{}'", namespacePath.getFullPath());
    }

    /**
     * To register Collection element to be rendered to the mod user HUD
     * @param elementsMap It's just a map of a namespacePath and a constructor reference
     * @see #register(NamespacePath, NewAbstractHudElementHandler)
     */
    public void register(@NotNull Map<NamespacePath, NewAbstractHudElementHandler> elementsMap)
    {
        for (Map.Entry<NamespacePath, NewAbstractHudElementHandler> entry : elementsMap.entrySet())
        {
            this.register(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Un recommended use this but if you really want to these removes an element from the registry
     * @param namespacePath Name space path for the element you want to get rid of
     * @deprecated Please don't use this
     */
    @Deprecated
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

    /**
     * Queries  the registry if an element exists
     * @param namespacePath A namespace path to an element to check if exists
     * @return Returns true if the element exists returns false otherwise
     */
    public boolean hasElement(@NotNull NamespacePath namespacePath)
    {
        return elementMap.containsKey(namespacePath);
    }

    /**
     * Gets a reference to the element constructor for use
     * @param namespacePath Name space path to the element
     * @return A reference to the element constructor which you have requested
     * @throws NoSuchElementException Throws this when the element cannot be found<br>
     * please use {@link #hasElement(NamespacePath)} beforehand
     * @implNote The {@link NoSuchElementException} is not referencing the hudElement itself but the back end {@link HashMap}
     */
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

    /**
     * Returns a read only view of the registry map might prove useful
     * @return Read only view of the registry map
     */
    public final @NotNull Map<NamespacePath, NewAbstractHudElementHandler> getElementMap()
    {
        return elementMap;
    }
}
