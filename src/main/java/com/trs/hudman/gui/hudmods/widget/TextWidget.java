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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class TextWidget extends AbstractHudWidget
{


    private String text = "";
    private int color = 0xFFFF;


    public TextWidget(int x, int y, float scale)
    {
        super(x, y, scale, 0);
    }

    public TextWidget(String text, int color, int x, int y, float scale)
    {
        super(x, y, scale, 0);
        this.text = text;
        this.color = color;
    }

    @Override
    protected void tick(){}

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, float partialTick)
    {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.scale(this.getScale(), this.getScale(), this.getScale());

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.literal(this.text), this.getX(), this.getY(), this.color);

        poseStack.popPose();
    }

    public final String getText()
    {
        return text;
    }

    public final void setText(String text)
    {
        this.text = text;
    }

    public final int getColor()
    {
        return color;
    }

    public final void setColor(int color)
    {
        this.color = color;
    }
}
