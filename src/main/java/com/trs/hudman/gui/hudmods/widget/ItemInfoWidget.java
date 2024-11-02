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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Internal
class ItemInfoWidget extends AbstractHudWidget
{

    public final ItemStack getItemStack()
    {
        return itemStack;
    }

    public final void setItemStack(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    private ItemStack itemStack = ItemStack.EMPTY;

    public ItemInfoWidget(int x, int y, float scale)
    {
        super(x, y, scale, 0);
    }

    @Override
    protected void tick()
    {

    }

    @Override
    public final void render(@NotNull GuiGraphics guiGraphics, float partialTick)
    {
        if (itemStack != ItemStack.EMPTY)
        {
            final List<Component> texts = List.of(
                    getItemNameAsComponent(itemStack),
                    Component.literal("")
            );

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            RenderSystem.enableBlend();

            // Coordinates where you want the tooltip box to appear
            int tooltipX = 100;
            int tooltipY = 100;

            // Render the tooltip at a fixed position
            guiGraphics.renderTooltip(
                    Minecraft.getInstance().font,
                    texts,
                    Optional.empty(),
                    tooltipX,
                    tooltipY
            );


            ItemWidget itemWidget = new ItemWidget(this.getX(), this.getY(), this.getScale());
            itemWidget.setItemStack(itemStack);

            RenderSystem.disableBlend();
            poseStack.popPose();
        }
    }

    private static Component getItemNameAsComponent(ItemStack itemStack) {
        return itemStack.getHoverName();
    }
}
