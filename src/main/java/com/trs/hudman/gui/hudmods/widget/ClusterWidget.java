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

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.trs.hudman.gui.hudmods.FPSElement;
import com.trs.hudman.util.NewAbstractHudElementHandler;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ClusterWidget extends AbstractHudWidget
{
    private final HashMap<String, AbstractHudWidget> widgetHashMap = new HashMap<>();

    private WidgetMetaData mkMetaData()
    {
        return WidgetMetaData.of(0, 0, null);
    }

    public ClusterWidget(int x, int y, float scale, int rotation)
    {
        super(x, y, scale, rotation);
    }

    public ClusterWidget(int x, int y, float scale, int rotation, Map<String, AbstractHudWidget> widget_map)
    {
        this(x, y, scale, rotation);
        for (Map.Entry<String, AbstractHudWidget> widget_set : widget_map.entrySet())
        {
            AbstractHudWidget widget = widget_set.getValue();
            widget.setX(this.getX() + widget.getX());
            widget.setY(this.getY() + widget.getY());
            widget.setScale(this.getScale() + widget.getScale());
            widgetHashMap.put(widget_set.getKey(), widget_set.getValue());
        }
    }

    public ClusterWidget(int x, int y, float scale)
    {
        this(x, y, scale, 0);
    }

    public ClusterWidget(int x, int y, float scale, Map<String, AbstractHudWidget> widget_map)
    {
        this(x, y, scale, 0, widget_map);
    }

    public final void clearWidgets()
    {
        widgetHashMap.clear();
    }

    public final void addWidget(String name, AbstractHudWidget widget)
    {
        widget.setX(this.getX() + widget.getX());
        widget.setY(this.getY() + widget.getY());
        widget.setScale(this.getScale() + widget.getScale());
        widgetHashMap.put(name, widget);
    }

    public final boolean hasWidget(String name)
    {
        return widgetHashMap.containsKey(name);
    }

    public final void removeWidget(String name)
    {
        widgetHashMap.remove(name);
    }

    public final AbstractHudWidget getWidget(String name)
    {
        return widgetHashMap.get(name);
    }

    @Override
    protected void tick()
    {
        for (AbstractHudWidget widget : widgetHashMap.values())
        {
            widget.widget_tick();
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, float partialTick)
    {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        //TODO: rotation
        for (AbstractHudWidget widget : widgetHashMap.values())
        {
            widget.render(guiGraphics, partialTick);
        }
        poseStack.popPose();
    }
}
