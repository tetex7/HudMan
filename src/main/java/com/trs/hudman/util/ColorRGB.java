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

import net.minecraft.core.Vec3i;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;

public record ColorRGB(int red, int green, int blue)
{
    public static final ColorRGB RED = ColorRGB.of(0xFF, 0, 0);
    public static final ColorRGB GREEN = ColorRGB.of( 0, 0xFF, 0);
    public static final ColorRGB BLUE = ColorRGB.of( 0, 0, 0xFF);
    public static final ColorRGB WHITE = ColorRGB.of( 0xFF, 0xFF, 0xFF);

    public static @NotNull ColorRGB formHexColorString(@NotNull String colorHexStr)
    {
        return ColorRGB.ofInt(
                Integer.parseUnsignedInt(
                        colorHexStr.substring(2).toLowerCase(),//.replace("0x", "").toLowerCase(),
                        16
                )
        );
    }

    public static @NotNull ColorRGB formVec3iColor(@NotNull Vec3i colorVec)
    {
        return ColorRGB.of(
                Math.clamp(colorVec.getX(), 0, 255),
                Math.clamp(colorVec.getY(), 0, 255),
                Math.clamp(colorVec.getZ(), 0, 255)
        );
    }

    public static @NotNull ColorRGB of(int red, int green, int blue)
    {
        return new ColorRGB(red, green, blue);
    }

    public static @NotNull ColorRGB ofInt(int color)
    {
        return new ColorRGB(color);
    }

    public int toRgbInt()
    {
        return (red << 16) | (green << 8) | blue;
    }

    public int toArgbInt(int alpha)
    {
        return ARGB.color(alpha, red, green, blue);
    }

    public int toArgbInt()
    {
        return toArgbInt(255);
    }

    public ColorRGB(int color)
    {
        this((color >> 16) & 0xFF,  (color >> 8) & 0xFF,  color & 0xFF);
    }

    public @NotNull String toRgbHexString()
    {
        return "0x" + Integer.toUnsignedString(this.toRgbInt(), 16);
    }

    public @NotNull String toArgbHexString()
    {
        return "0x" + Integer.toUnsignedString(this.toArgbInt(), 16);
    }

    public @NotNull ColorRGB plus(@NotNull ColorRGB color2)
    {
        int red = Math.min(this.red() + color2.red(), 255);
        int green = Math.min(this.green() + color2.green(), 255);
        int blue = Math.min(this.blue() + color2.blue(), 255);

        return ColorRGB.of(red, green, blue);
    }

    public @NotNull ColorRGB minus(@NotNull ColorRGB color2)
    {
        int red = Math.clamp(this.red() - color2.red(), 0, 255);
        int green = Math.clamp(this.green() - color2.green(), 0, 255);
        int blue = Math.clamp(this.blue() - color2.blue(), 0, 255);

        return ColorRGB.of(red, green, blue);
    }

    @Override
    public @NotNull String toString()
    {
        return toRgbHexString();
    }
}
