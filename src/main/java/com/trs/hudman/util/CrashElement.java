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

package com.trs.hudman.util;
import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.gui.hudmods.AbstractHudElement;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class CrashElement extends AbstractHudElement
{
    private final int ticks;
    public CrashElement(int ticks)
    {
        super(
                null,
                Minecraft.getInstance(),
                Vec2i.of(0, 0),
                new JsonConfigHudElement(
                        NamespacePath.pathOf("NAP"),
                        new Vec2i(0, 0),
                        0,
                        0,
                        0f,
                        "",
                        true,
                        List.of("")
                )
        );
        this.ticks = ticks;
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui) {}

    private int t = 0;

    @Override
    public void tick()
    {
        if (t >= ticks)
        {
            CrashReport crashReport = CrashReport.forThrowable(new Throwable(),"Crash caused by Intentionally Malform Element");
            throw new ReportedException(crashReport);
        }
        t++;
    }
}
