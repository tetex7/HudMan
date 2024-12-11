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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public interface IRegistry<T>
{

    void register(@NotNull NamespacePath namespacePath, T value);
    void register(@NotNull Map<NamespacePath, T> values);
    boolean hasElement(@NotNull NamespacePath namespacePath);
    @NotNull T get(@NotNull NamespacePath namespacePath);
    @NotNull Map<NamespacePath, T> getRegistryMap();

    @Deprecated
    void unregister(@NotNull NamespacePath namespacePath);
}
