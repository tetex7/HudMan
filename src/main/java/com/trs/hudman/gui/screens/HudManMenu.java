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

package com.trs.hudman.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.*;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class HudManMenu extends Screen
{
    //private final Screen lastScreen;
    private static final Component TestText = Component.literal("Test");
    private Button Test;
    private final Screen lastScreen;
    private static final int COLUMNS = 2;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 61, 33);


    public HudManMenu(Screen lastScreen)
    {
        super(Component.translatable("string.hudman.mod_name"));
        this.lastScreen = lastScreen;
    }

    @Override
    public void onClose()
    {
        this.minecraft.setScreen(lastScreen);
    }

    @Override
    public void tick()
    {
        super.tick();
    }

    @Override
    protected void init()
    {
        /*Test = this.addRenderableWidget(Button.builder(Component.translatable("gui.ok"), (pButton)-> {
            this.onClose();
        }).build());*/
        LinearLayout linearLayout = this.layout.addToHeader(LinearLayout.vertical().spacing(8));
        linearLayout.addChild(new StringWidget(Component.translatable("string.hudman.mod_name"), this.font), LayoutSettings::alignHorizontallyCenter);
        LinearLayout linearLayout2 = linearLayout.addChild(LinearLayout.horizontal()).spacing(8);
        GridLayout gridLayout = new GridLayout();
        gridLayout.defaultCellSetting().paddingHorizontal(4).paddingBottom(4).alignHorizontallyCenter();
        RowHelper rowHelper = gridLayout.createRowHelper(2);

        rowHelper.addChild(this.openScreenButton(Component.literal("test"), () -> new HudManMenu(this)));
        rowHelper.addChild(this.openScreenButton(Component.literal("test"), () -> new HudManMenu(this)));

        this.layout.addToContents(gridLayout);
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, buttonx -> this.onClose()).width(200).build());
        this.layout.visitWidgets(guiEventListener -> {
            AbstractWidget var10000 = this.addRenderableWidget(guiEventListener);
        });
        this.repositionElements();
    }


    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
    {
        //this.renderDirtBackground(pGuiGraphics);
        //this.renderPngBackground(pGuiGraphics);
        //this.renderBackground(pGuiGraphics);
        //pGuiGraphics.drawString(this.font, TestText, this.width / 2 - 100, 53, 10526880);
        //super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        //this.Test.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private Button openScreenButton(Component name, Supplier<Screen> screenSupplier) {
        return Button.builder(name, button -> this.minecraft.setScreen((Screen)screenSupplier.get())).build();
    }
}
