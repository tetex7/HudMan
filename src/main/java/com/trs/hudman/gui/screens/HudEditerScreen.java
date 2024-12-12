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

package com.trs.hudman.gui.screens;

import java.util.function.Supplier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class HudEditerScreen extends Screen
{
    //private final Screen lastScreen;
    private static final Component TestText = Component.literal("Test");
    private Button Test;

    public HudEditerScreen(Component title)
    {
        super(title);
        //lastScreen = LastScreen;
    }


    public void tick()
    {
        super.tick();
    }

    @Override
    protected void init()
    {
        Test = this.addRenderableWidget(Button.builder(Component.translatable("gui.ok"), (pButton)-> {
            this.onClose();
        }).build());
        super.init();

    }

    void saveHud()
    {

    }

    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
    {
        //this.renderDirtBackground(pGuiGraphics);
        //this.renderPngBackground(pGuiGraphics);
        //this.renderBackground(pGuiGraphics);
        pGuiGraphics.drawString(this.font, TestText, this.width / 2 - 100, 53, 10526880);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.Test.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
