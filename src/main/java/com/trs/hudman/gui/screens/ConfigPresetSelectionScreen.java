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

/*
This config screen is not functional so far this is a work in progress
And no piece of code will Utilize it so far
*/


import com.trs.hudman.util.NamespacePath;
import dev.lambdaurora.spruceui.Tooltip;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.util.ScissorManager;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceLabelWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
//import dev.lambdaurora.spruceui.widget.container.SpruceEntryListWidget;

public class ConfigPresetSelectionScreen extends SpruceScreen
{
    private final Screen last_screen;
    //private SpruceEntryListWidget<?> presetList;


    public ConfigPresetSelectionScreen(Component title, Screen last)
    {
        super(title);
        last_screen = last;
        //presetList = new SpruceEntryListWidget<>();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        ScissorManager.pushScaleFactor(this.scaleFactor);
        renderScreenBackground(guiGraphics);
        this.renderWidgets(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTitle(guiGraphics, mouseX, mouseY, partialTick);
        Tooltip.renderAll(guiGraphics);
        ScissorManager.popScaleFactor();
        guiGraphics.drawCenteredString(font,  Component.literal("This is a work in progress and is currently broken"), width/2, height/2, 0xFFFFFF);
    }

    @Override
    public void onClose()
    {
        this.minecraft.setScreen(last_screen);
    }

    @Override
    protected void init()
    {
        Button ok_button = Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
                .pos(0, this.height-20)
                .width((this.width/4))
                .build();
        this.addRenderableWidget(ok_button);
    }

    @Override
    public void tick()
    {
        super.tick();
    }


    void renderScreenBackground(GuiGraphics guiGraphics)
    {
        super.renderDirtBackground(guiGraphics);
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0x60101010, 0x60101010);
        guiGraphics.blit(BACKGROUND_LOCATION, 0, 0, 0, 0.0F, 0.0F, this.width/4, this.height, 32, 32);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.fillGradient(0, 0, this.width/4, this.height, 0x62101008, 0x62101008);
    }
}
