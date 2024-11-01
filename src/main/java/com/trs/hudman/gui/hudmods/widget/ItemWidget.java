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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ItemWidget extends AbstractHudWidget
{

    public ItemStack getItemStack()
    {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    private ItemStack itemStack = ItemStack.EMPTY;

    public ItemWidget(int x, int y, float scale)
    {
        super(x, y, scale);
    }

    @Override
    protected void tick()
    {

    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, float partialTick)
    {
        if (itemStack != ItemStack.EMPTY)
        {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            RenderSystem.enableBlend();
            poseStack.scale(this.getScale(), this.getScale(), this.getScale());
            guiGraphics.renderFakeItem(itemStack, this.getX(), this.getY());
            RenderSystem.disableBlend();
            poseStack.popPose();
        }
    }
}
