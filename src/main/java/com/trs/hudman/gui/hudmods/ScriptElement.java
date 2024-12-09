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

import com.trs.hudman.HudState;
import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.util.NamespacePath;
import com.trs.hudman.util.ScriptEnvironment;
import com.trs.hudman.util.Vec2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScriptElement extends AbstractHudElement
{
    private final ScriptEnvironment script;

    /**
     * @param root        Mostly time it's null and will probably be removed
     * @param client      The current Minecraft client
     * @param cords       The coordinates of the element on the user screen
     * @param jsonElement The Jason config structure turned into a Java class
     */
    public ScriptElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i cords, @NotNull JsonConfigHudElement jsonElement)
    {
        super(root, client, cords, jsonElement);

        if (getJsonElement().strings().isEmpty())
        {
            throw new RuntimeException("Improper configuration");
        }
        NamespacePath path = NamespacePath.of(getJsonElement().strings().get(getJsonElement().strings().size()-1));
        if (!HudState.scripts.isEmpty() && HudState.scripts.containsKey(path))
        {
            script = HudState.scripts.get(path);
        }
        else
        {
            throw new RuntimeException("No scripts found ");
        }
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        script.callRender(gui.getFont(), guiGraphics, partialTick);
    }

    @Override
    public void tick()
    {
        script.callTick();
    }
}
