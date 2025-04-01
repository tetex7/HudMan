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
import com.mojang.math.Axis;
import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.util.Vec2i;
import com.trs.hudman.util.annotations.RegistrableHudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

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
    public void render3d(float partialTick, PoseStack matrixStack, GuiGraphics guiGraphics, Gui gui)
    {
        rotationAngle += partialTick * 2;
        matrixStack.pushPose();
        //VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.debugLineStrip(1.0));

        //guiGraphics.drawCenteredString(gui.getFont(), String.valueOf(ThreadLocalRandom.current().nextInt()), 100, 100, 0xFFFFFF);
        Matrix4f matrix4f = matrixStack.last().pose();
        //matrixStack.translate(getCords().x(), getCords().y(), 10);

        matrixStack.scale(getScale(), getScale(), getScale()); // Scale to fit GUI
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        drawCube(buffer);
        BufferUploader.draw(Objects.requireNonNull(buffer.build()));

        matrixStack.popPose();
    }

    @Override
    public void render2d(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {

    }

    @Override
    public void tick()
    {
    }

    private void drawCube(BufferBuilder buffer)
    {
        // Define cube vertices and colors
        //buffer.addVertex(getCords().x(), getCords().y() +4, -1).setColor(255, 0, 0, 255);
        //buffer.addVertex( -1, -1, -1).setColor(0, 255, 0, 255);
        //buffer.addVertex( 1,  1, -1).setColor(0, 0, 255, 255);
        //buffer.addVertex(-1,  1, -1).setColor(255, 255, 0, 255);
        // Other faces...

        buffer.addVertex(getCords().x(), getCords().y(), 2).setColor(0xFF, 0, 0, 0).setNormal(0, 0, 1);
        buffer.addVertex(getCords().x()+24, getCords().y(), 0).setColor(0xFF, 0, 0, 0).setNormal(0, 0, 1);
        buffer.addVertex(getCords().x(), getCords().y()+24, -2).setColor(0xFF, 0, 0, 0).setNormal(0, 0, 1);
    }
}
