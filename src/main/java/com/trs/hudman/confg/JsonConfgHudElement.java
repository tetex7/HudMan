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

package com.trs.hudman.confg;

import com.trs.hudman.util.Vec2i;

public record JsonConfgHudElement(
        String elementId,
        Vec2i cords,
        int width,
        int height,
        float scale,
        //int NodeId,
        String pairGameHudElement,
        boolean enable,
        String[] strings

)
{
    public static JsonConfgHudElement copy(JsonConfgHudElement element)
    {
        return new JsonConfgHudElement(
                new String(element.elementId),
                new Vec2i(element.cords.x(), element.cords.y()),
                element.width,
                element.height,
                element.scale,
                new String(element.pairGameHudElement),
                element.enable,
                element.strings.clone()
        );
    }
}
