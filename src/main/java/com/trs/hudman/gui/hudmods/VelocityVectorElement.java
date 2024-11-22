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

package com.trs.hudman.gui.hudmods;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.gui.hudmods.widget.ClusterWidget;
import com.trs.hudman.gui.hudmods.widget.FlowMeterWidget;
import com.trs.hudman.gui.hudmods.widget.TextWidget;
import com.trs.hudman.mixin.PlayerGuiMixinAccessor;
import com.trs.hudman.util.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.trs.hudman.HudState;

import java.util.Map;

@Environment(EnvType.CLIENT)
public final class VelocityVectorElement extends AbstractHudElement
{
    private Vec3 player_velocity = new Vec3(0,0, 0);
    private Vec3 player_cords = new Vec3(0, 0, 0);
    private Vec3 dta_cords = new Vec3(0, 0, 0);

    private final boolean doTooltip = !this.getJsonElement().strings().isEmpty() && this.getJsonElement().strings().get(this.getJsonElement().strings().size() - 1).equalsIgnoreCase("dotooltip");

    /*private final FlowMeterWidget X_VECTOR_METER = new FlowMeterWidget(-25, 0, 0f);
    private final FlowMeterWidget Y_VECTOR_METER = new FlowMeterWidget(0, 0, 0f);
    private final FlowMeterWidget Z_VECTOR_METER = new FlowMeterWidget(25, 0, 0f);*/

    private final ClusterWidget METER_CLUSTER = new ClusterWidget(
            getCords().x(),
            getCords().y(),
            1,
            90,
            Map.of(

                    "X_VECTOR_METER", new FlowMeterWidget(-25, 0, 0f),
                    "X_METER", new TextWidget("X", 0xFFFFFF, -14, 66, 0f),
                    "Y_VECTOR_METER", new FlowMeterWidget(0, 0, 0f),
                    "Y_METER", new TextWidget("Y", 0xFFFFFF, 11, 66, 0f),
                    "Z_VECTOR_METER", new FlowMeterWidget(25, 0, 0f),
                    "Z_METER", new TextWidget("Z", 0xFFFFFF, 36, 66, 0f)
            )
    );

    private Vec3 delta = player_velocity;

    public VelocityVectorElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i rCords, @NotNull JsonConfigHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        if (HudState.getConfigDebug())
        {
            guiGraphics.drawCenteredString(gui.getFont(), delta.toString(), (((PlayerGuiMixinAccessor)gui).getScreenWidth() / 2), ((((PlayerGuiMixinAccessor)gui).getScreenHeight() / 2) + 5) + 5, 0xFFFFFF);
        }

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.scale(this.getJsonElement().scale(), this.getJsonElement().scale(), this.getJsonElement().scale());
        RenderSystem.enableBlend();
        if (doTooltip)
        {
            guiGraphics.flush();
            TooltipRenderUtil.renderTooltipBackground(guiGraphics, this.getCords().x() - 25, this.getCords().y(), 72, 74, -1);
            guiGraphics.flush();
        }
        RenderSystem.disableBlend();
        METER_CLUSTER.render(guiGraphics, partialTick);
        poseStack.popPose();
    }

    @Override
    public void tick()
    {
        player_velocity = getPlayer().getDeltaMovement();
        player_cords = new Vec3(getPlayer().getX(), getPlayer().getY(), getPlayer().getZ());
        dta_cords = player_cords.add(player_velocity);
        delta = getAmpDelta(player_velocity);

        ((FlowMeterWidget)METER_CLUSTER.getWidget("X_VECTOR_METER")).setValue(Double.valueOf(delta.x()).intValue());
        ((FlowMeterWidget)METER_CLUSTER.getWidget("Y_VECTOR_METER")).setValue(Double.valueOf(delta.y()).intValue());
        ((FlowMeterWidget)METER_CLUSTER.getWidget("Z_VECTOR_METER")).setValue(Double.valueOf(delta.z()).intValue());

        METER_CLUSTER.widget_tick();
    }

    private static @NotNull Vec3 getAmpDelta(Vec3 cord_mov)
    {
        Vec3 amp = new Vec3(
                cord_mov.x * 25,
                cord_mov.y * 10,
                cord_mov.z * 25
        );

        return new Vec3(
                clampToInt(Mth.clamp(amp.x, -39, 39) / 1.9),
                clampToInt(Mth.clamp(amp.y, -39, 39) / 1.9),
                clampToInt(Mth.clamp(amp.z, -39, 39) / 1.9)
        );
    }

    private static double clampToInt(double v)
    {
        return Integer.valueOf(Double.valueOf(v).intValue()).doubleValue();
    }
}
