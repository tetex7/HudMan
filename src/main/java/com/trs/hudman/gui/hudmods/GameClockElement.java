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

import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.gui.hudmods.widget.ItemWidget;
import com.trs.hudman.util.Vec2i;
import com.trs.hudman.util.annotations.RegistrableHudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RegistrableHudElement(regName = "game_clock")
public class GameClockElement extends AbstractHudElement
{
    final ItemWidget gclock;

    /**
     * @param root        Mostly time it's null and will probably be removed
     * @param client      The current Minecraft client
     * @param cords       The coordinates of the element on the user screen
     * @param jsonElement The Jason config structure turned into a Java class
     * @implSpec Your constructor using this super class must contain all four
     */
    public GameClockElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i cords, @NotNull JsonConfigHudElement jsonElement)
    {
        super(root, client, cords, jsonElement);
        this.gclock = new ItemWidget(getCords().x(), getCords().y(), getScale()+1.5f, Items.CLOCK);
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        gclock.render(guiGraphics, partialTick);
    }

    @Override
    public void tick()
    {
        gclock.widgetTick();
    }
}
