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

package com.trs.hudman.gui.hudmods.widget;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Stack;

public abstract class AbstractHudWidget
{
    private final Stack<OnTickHandler> tickHandlers = new Stack<>();

    public final void addTickHandler(OnTickHandler handler)
    {
        tickHandlers.push(handler);
    }

    private int x;
    private int y;
    private float scale;

    private final int rotation;
    private WidgetMetaData metaData;


    AbstractHudWidget(int x, int y, float scale, int rotation)
    {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.rotation = rotation;
    }

    protected abstract void tick();

    public final void widget_tick()
    {
        for (OnTickHandler handler : tickHandlers)
        {
            handler.work(this);
        }
        tick();
    }

    public abstract void render(@NotNull GuiGraphics guiGraphics, float partialTick);

    public final int getX()
    {
        return x;
    }

    public final void setX(int x)
    {
        this.x = x;
    }

    public final int getY()
    {
        return y;
    }

    public final void setY(int y)
    {
        this.y = y;
    }

    public final float getScale()
    {
        return scale;
    }

    public final void setScale(float scale)
    {
        this.scale = scale;
    }

    protected final WidgetMetaData getMetaData()
    {
        return metaData;
    }

    protected final void setMetaData(WidgetMetaData metaData)
    {
        this.metaData = metaData;
    }

    public final int getRotation()
    {
        return rotation;
    }

    @FunctionalInterface
    public interface OnTickHandler
    {
        void work(AbstractHudWidget this_widget);
    }


    protected record WidgetMetaData(
            int width,
            int height,
            @Nullable Map<String, ?> userdata
    ) {
        public static @NotNull WidgetMetaData of(int width, int height, @Nullable Map<String, ?> userdata)
        {
            return new WidgetMetaData(width, height, userdata);
        }

        public @NotNull WidgetMetaData copy()
        {
            return new WidgetMetaData(
                    this.width,
                    this.height,
                    this.userdata == null ? null : Map.copyOf(userdata)
            );
        }
    }
}
