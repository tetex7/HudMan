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
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.trs.hudman.HudState;

public final class FlowMeterWidget extends AbstractHudWidget
{
    public static final ResourceLocation FLOW_METER_BODY_LOCATION = new ResourceLocation(HudState.MODID, "textures/gui/hudmods/widget/flow_meter_widget/flow_meter_body.png");
    public static final ResourceLocation FLOW_METER_POINT_LOCATION = new ResourceLocation(HudState.MODID, "textures/gui/hudmods/widget/flow_meter_widget/flow_meter_point.png");

    private int value = 0;

    public FlowMeterWidget(int x, int y, float scale)
    {
        super(x, y, scale, 0);
        super.setMetaData(WidgetMetaData.of(22, 62, null));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, float partialTick)
    {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        //poseStack.translate((this.getX() - 11), (this.getY() - 32), 0);
        poseStack.scale(this.getScale(), this.getScale(), this.getScale());



        float angle = super.getRotation(); // Rotation angle in degrees
        float pointX = 0; // X-coordinate of the rotation center
        float pointY = 0; // Y-coordinate of the rotation center
        float pointZ = 0;  // Z-coordinate, set to 0 for 2D plane

        poseStack.rotateAround(Axis.ZP.rotationDegrees(angle), pointX, pointY, pointZ);

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

    public int getValue()
    {
        return value;
    }

    public void setValue(@Range(from = -25, to = 25) int value)
    {
        this.value = value;
    }
}
