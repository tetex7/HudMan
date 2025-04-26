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
import com.trs.hudman.util.Vec2i;
import com.trs.hudman.util.annotations.RegistrableHudElement;
import com.trs.qlang.Qlang;
import com.trs.qlang.QlangInstruction;
import com.trs.qlang.QlangString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RegistrableHudElement(regName = "cords")
public class CordsElement extends AbstractHudElement
{
    private Vec3i cords;

    private final QlangString raw_sting;
    Component Text = Component.literal("test");

    public static String X_TAG = "%X%";
    public static String Y_TAG = "%Y%";
    public static String Z_TAG = "%Z%";

    final boolean isCenter;

    private final List<ImmutablePair<Pattern, QlangInstruction>> coordinateTaglibrary = List.of(
            ImmutablePair.of(Pattern.compile(X_TAG), QlangInstruction.of((tag, pattern) -> Integer.toString(cords.getX()))),
            ImmutablePair.of(Pattern.compile(Y_TAG), QlangInstruction.of((tag, pattern) -> Integer.toString(cords.getY()))),
            ImmutablePair.of(Pattern.compile(Z_TAG), QlangInstruction.of((tag, pattern) -> Integer.toString(cords.getZ())))
    );

    public CordsElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i cords, @NotNull JsonConfigHudElement jsonElement)
    {
        super(root, client, cords, jsonElement);

        this.raw_sting = getConfigCordNotation();

        if (super.hasStringOption("bCenter"))
        {
            this.isCenter = getStringOptionAs("bCenter", Boolean::parseBoolean);
        }
        else
        {
            this.isCenter = false;
        }

        raw_sting.getQLangInterpreter().AddAllInstructions(coordinateTaglibrary);
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        doScaleSafeEnvironment(guiGraphics, () -> {
            if (!isCenter)
            {
                guiGraphics.drawCenteredString(gui.getFont(), Text, 0, 0, 0xFFFFFF);
            }
        });
        if (isCenter)
        {
            guiGraphics.drawCenteredString(gui.getFont(), Text, guiGraphics.guiWidth() / 2, 0, 0xFFFFFF);
        }
    }

    @Override
    public void tick()
    {
        cords = new Vec3i(
                (int)getPlayer().getX(),
                (int)getPlayer().getY(),
                (int)getPlayer().getZ()
        );
        Text = Component.literal(raw_sting.toString());
    }

    private QlangString getConfigCordNotation()
    {
        if (getJsonElement().strings().isEmpty() || getJsonElement().strings().get(0).startsWith("bCenter") || getJsonElement().strings().get(0).isEmpty())
        {
            return QlangString.ofLiteral("X(%X%), Z(%Z%), Y(%Y%)");
        }
        else
        {
            return QlangString.ofLiteral(getJsonElement().strings().get(0));
        }
    }
}
