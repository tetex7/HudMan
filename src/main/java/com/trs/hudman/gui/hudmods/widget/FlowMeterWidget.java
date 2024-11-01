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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.trs.hudman.HudState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class FlowMeterWidget extends AbstractHudWidget
{
    public static final ResourceLocation FLOW_METER_BODY_LOCATION = new ResourceLocation(HudState.getModid(), "textures/gui/hudmods/widget/flow_meter_widget/flow_meter_body.png");
    public static final ResourceLocation FLOW_METER_POINT_LOCATION = new ResourceLocation(HudState.getModid(), "textures/gui/hudmods/widget/flow_meter_widget/flow_meter_point.png");

    private int value = 0;

    public FlowMeterWidget(int x, int y, float scale)
    {
        super(x, y, scale);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, float partialTick)
    {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.scale(this.getScale(), this.getScale(), this.getScale());
        RenderSystem.enableBlend();
        guiGraphics.blit(
                FLOW_METER_BODY_LOCATION,
                this.getX(),
                this.getY(),
                0,0, 22, 62, 22, 62
        );
        guiGraphics.blit(
                FLOW_METER_POINT_LOCATION,
                this.getX() + 5,
                ((this.getY() + 25) - value),
                0,0, 12, 12, 12, 12
        );
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    @Override
    protected void tick() {}

    public final int getValue()
    {
        return value;
    }

    public final void setValue(@Range(from = -25, to = 25) int value)
    {
        this.value = value;
    }
}
