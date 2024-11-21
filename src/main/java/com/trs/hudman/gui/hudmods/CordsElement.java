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

import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.util.Vec2i;
import com.trs.qlang.Qlang;
import com.trs.qlang.QlangInstruction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

public class CordsElement extends AbstractHudElement
{
    private Vec3i cords;

    private String raw_sting;
    Component Text = Component.literal("test");

    public static String X_TAG = "%X%";
    public static String Y_TAG = "%Y%";
    public static String Z_TAG = "%Z%";

    boolean isCenter;

    private final Qlang parser = Qlang.builder().AddAllInstructions(
            ImmutablePair.of(Pattern.compile(X_TAG), QlangInstruction.of((tag, pattern) -> Integer.toString(cords.getX()))),
            ImmutablePair.of(Pattern.compile(Y_TAG), QlangInstruction.of((tag, pattern) -> Integer.toString(cords.getY()))),
            ImmutablePair.of(Pattern.compile(Z_TAG), QlangInstruction.of((tag, pattern) -> Integer.toString(cords.getZ())))
    ).build();

    public CordsElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i cords, @NotNull JsonConfigHudElement jsonElement)
    {
        super(root, client, cords, jsonElement);

        List<String> strings = List.of(getJsonElement().strings());

        if (strings.isEmpty() || strings.get(0).equalsIgnoreCase("center") || strings.get(0).isEmpty())
        {
            raw_sting = "X(%X%), Z(%Z%)), Y(%Y%)";
        }
        else
        {
            raw_sting = strings.get(0);
        }

        for(String string : getJsonElement().strings())
        {
            if (string.equalsIgnoreCase("center"))
            {
                isCenter = true;
                break;
            }
        }
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        if (isCenter)
        {
            guiGraphics.drawCenteredString(gui.getFont(), Text, guiGraphics.guiWidth() / 2, getCords().y(), 0xFFFFFF);
        }
        else
        {
            guiGraphics.drawCenteredString(gui.getFont(), Text, getCords().x(), getCords().y(), 0xFFFFFF);
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
        Text = Component.literal(parser.parse(raw_sting).outputString());
    }
}
