/*
 * Copyright (C) 2025  Tetex7
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

import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.util.NamespacePath;
import com.trs.hudman.util.Vec2i;
import com.trs.hudman.util.annotations.RegistrableHudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.SimpleTexture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RegistrableHudElement(regName = "image")
public class ImageElement extends AbstractHudElement
{
    private final NamespacePath imagePath;

    /**
     * @param root        Mostly time it's null and will probably be removed
     * @param client      The current Minecraft client
     * @param cords       The coordinates of the element on the user screen
     * @param jsonElement The Json config structure turned into a Java class
     * @implSpec Your constructor using this super class must contain all four
     */
    public ImageElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i cords, @NotNull JsonConfigHudElement jsonElement)
    {
        super(root, client, cords, jsonElement);
        imagePath = getStringOptionAs("npPath", NamespacePath::of);
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        doScaleSafeEnvironment(guiGraphics, () -> {
            try
            {
                var da = ((SimpleTexture) getClient().getTextureManager().getTexture(imagePath.getResourceLocation())).loadContents(getClient().getResourceManager()).image();
                guiGraphics.blit(
                        RenderType::guiTextured,
                        imagePath.getResourceLocation(),
                        0,
                        0,
                        0,
                        0,
                        getJsonElement().width(),
                        getJsonElement().height(),
                        da.getWidth(),
                        da.getHeight()
                );
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void tick()
    {

    }
}
