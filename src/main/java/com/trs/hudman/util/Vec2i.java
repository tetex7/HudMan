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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public record Vec2i(int x, int y)
{
    public static Vec2i of(int x, int y)
    {
        return new Vec2i(x, y);
    }

    public @NotNull Vec2i plus(@NotNull Vec2i vec)
    {
        return new Vec2i((this.x + vec.x),(this.y + vec.y));
    }

    public @NotNull Vec2i minus(@NotNull Vec2i vec)
    {
        return new Vec2i((this.x - vec.x),(this.y - vec.y));
    }

    public Vector2ic toGLvec2i()
    {
        return new Vector2i(this.x, this.y);
    }
}
