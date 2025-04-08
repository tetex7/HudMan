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

package com.trs.hudman.gui.hudmods;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.util.ColorRGB;
import com.trs.hudman.util.Vec2i;
import com.trs.hudman.util.annotations.RegistrableHudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

@SuppressWarnings("unused")
@RegistrableHudElement(regName = "test_3d_cube")
public final class Test3DCubeElement extends AbstractHud3DElement
{

    private float rotationAngle = 0;

    public Test3DCubeElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i rCords, @NotNull JsonConfigHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);
    }

    @Override
    public void render3d(MultiBufferSource bufferSource, PoseStack matrixStack, float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        rotationAngle += partialTick * 0.25f;
        matrixStack.pushPose();
        Matrix4f matrix = matrixStack.last().pose();

        matrixStack.translate(getCords().x(), getCords().y(), 0);

        matrixStack.scale(getScale(), getScale(), 0); // Scale to fit GUI
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.debugQuads());

        drawCube(vertexConsumer, matrix);

        matrixStack.popPose();

        guiGraphics.flush();

    }

    @Override
    public void render2d(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {

    }

    @Override
    public void tick()
    {
    }


    private void drawCube(VertexConsumer buffer,  Matrix4f matrix)
    {
        // Define cube vertices and colors
        //buffer.addVertex(getCords().x(), getCords().y() +4, -1).setColor(255, 0, 0, 255);
        //buffer.addVertex( -1, -1, -1).setColor(0, 255, 0, 255);
        //buffer.addVertex( 1,  1, -1).setColor(0, 0, 255, 255);
        //buffer.addVertex(-1,  1, -1).setColor(255, 255, 0, 255);
        // Other faces...
        buffer.addVertex(matrix, getCords().x(), getCords().y(), 0).setColor(ColorRGB.RED.toArgbInt());
        buffer.addVertex(matrix, getCords().x()+24, getCords().y(), 0).setColor(ColorRGB.RED.toArgbInt());
        buffer.addVertex(matrix, getCords().x(), getCords().y()+4, 0).setColor(ColorRGB.RED.toArgbInt());
        buffer.addVertex(matrix, getCords().x()+24, getCords().y()+4, 0).setColor(ColorRGB.RED.toArgbInt());
        //buffer.addVertex(getCords().x(), getCords().y()+24, 0).setColor(ColorRGB.RED.toArgbInt());
        //buffer.addVertex(matrix, getCords().x(), getCords().y()+24, 0).setColor(ColorRGB.RED.toArgbInt(256));
    }
}
