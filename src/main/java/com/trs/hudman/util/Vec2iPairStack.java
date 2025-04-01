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

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Stack;

public final class Vec2iPairStack
{
    private final Stack<Vec2iPair> inerStack = new Stack<>();


    public void push(Vec2i start, Vec2i end)
    {
        inerStack.push(Vec2iPair.of(start, end));
    }

    public Vec2iPair get(int index)
    {
        return inerStack.get(index);
    }

    public Vec2iPair peek()
    {
        return inerStack.peek();
    }

    public Vec2iPair pop()
    {
        return inerStack.pop();
    }

    public int getSize()
    {
        return inerStack.size();
    }

    public Stack<Vec2iPair> getRawStack()
    {
        return inerStack;
    }

    public record Vec2iPair(Vec2i start, Vec2i end)
    {
        public static Vec2iPair of(Vec2i start, Vec2i end)
        {
            return new Vec2iPair(start, end);
        }
    }
}
