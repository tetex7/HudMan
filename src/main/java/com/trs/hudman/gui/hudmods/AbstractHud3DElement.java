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

import com.mojang.blaze3d.vertex.PoseStack;
import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.util.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class AbstractHud3DElement extends AbstractHudElement
{
    public AbstractHud3DElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i rCords, @NotNull JsonConfigHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);
    }


    @Override
    public final void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        render3d(guiGraphics.pose(), guiGraphics, gui);
        render2d(guiGraphics, gui);
    }

    public abstract void render3d(PoseStack matrixStack, GuiGraphics guiGraphics, Gui gui);


    public abstract void render2d(GuiGraphics guiGraphics, Gui gui);
}
