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

import com.trs.hudman.HudState;
import com.trs.hudman.confg.ConfigHelper;
import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.confg.JsonConfigHudPreset;
import com.trs.hudman.util.ElementRegistry;
import com.trs.hudman.util.Vec2i;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class HudPresetClusterElement extends AbstractHudElement
{

    public static HudPresetClusterElement fromHudPresetsJsonFile(Minecraft minecraft, JsonConfigHudElement jsonElement, JsonConfigHudPreset jsonPreset)
    {
        return new HudPresetClusterElement(null, minecraft, jsonElement.cords(), jsonElement, jsonPreset);
    }

    private final JsonConfigHudPreset jsonPreset;
    private final Stack<AbstractHudElement> subs = new Stack<>();

    private HudPresetClusterElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i cords, @NotNull JsonConfigHudElement jsonElement, @NotNull JsonConfigHudPreset jsonPreset)
    {
        super(root, client, cords, jsonElement);
        this.jsonPreset = jsonPreset;

        if (!this.jsonPreset.subElements().isEmpty())
        {
            for (final JsonConfigHudElement element : this.jsonPreset.subElements())
            {
                if (HudState.elementRegistry.hasElement(element.elementId()))
                {
                    try
                    {
                        subs.push(HudState.elementRegistry.get(element.elementId()).create(
                                null,
                                client,
                                Vec2i.of(
                                        element.cords().x() + cords.x(),
                                        element.cords().y() + cords.y()
                                ),
                                element
                        ));
                    }
                    catch (Exception e)
                    {
                        HudState.LOGGER.error("Exception on Initializing HudElement '{}' in Cluster '{}'\n{}",
                                element.elementId(),
                                jsonElement.elementId(),
                                ConfigHelper.stackTraceString(e)
                        );
                    }
                }
                else
                {
                    HudState.LOGGER.error("no Element by ElementName:'{}' on Namespace:'{}'", element.elementId().getPath(), element.elementId().getNamespace());
                }
            }
        }
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        if (!subs.isEmpty())
        {
            for (final AbstractHudElement element : subs)
            {
                if (element.getJsonElement().enable())
                {
                    element.render(partialTick, guiGraphics, gui);
                }
            }
        }
    }

    @Override
    public void tick()
    {
        if (!subs.isEmpty())
        {
            for (final AbstractHudElement element : subs)
            {
                if (element.getJsonElement().enable())
                {
                    element.tick();
                }
            }
        }
    }
}
