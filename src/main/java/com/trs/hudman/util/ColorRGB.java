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

import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;

public record ColorRGB(int red, int green, int blue)
{
    public static final ColorRGB RED = ColorRGB.of(0xFF, 0, 0);
    public static final ColorRGB GREEN = ColorRGB.of( 0, 0xFF, 0);
    public static final ColorRGB BLUE = ColorRGB.of( 0, 0, 0xFF);
    public static final ColorRGB WHITE = ColorRGB.of( 0xFF, 0xFF, 0xFF);

    public static @NotNull ColorRGB of(int red, int green, int blue)
    {
        return new ColorRGB(red, green, blue);
    }

    public static @NotNull ColorRGB ofInt(int color)
    {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        return new ColorRGB(red, green, blue);
    }

    public int toRgbInt()
    {
        return (red << 16) | (green << 8) | blue;
    }

    public int toArgbInt(int alpha)
    {
        return ARGB.color(alpha, red, green, blue);
    }
}
