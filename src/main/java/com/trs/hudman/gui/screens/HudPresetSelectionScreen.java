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

import com.trs.hudman.HudState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HudPresetSelectionScreen extends Screen {
    private HudPresetList fileList;
    private final Screen parentScreen;
    private final File directory = new File(HudState.configDirPath + "/hudman_presets");
    private List<File> files;

    public HudPresetSelectionScreen(Screen parent) {
        super(Component.literal("Select a File"));
        this.parentScreen = parent;
    }

    @Override
    protected void init() {
        this.files = loadFiles(directory);
        this.fileList = new HudPresetList(this.minecraft, this, files);
        this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> this.minecraft.setScreen(parentScreen))
                .bounds(this.width / 2 - 100, this.height - 30, 200, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        this.fileList.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private List<File> loadFiles(File directory) {
        List<File> list = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    private static class HudPresetList extends ObjectSelectionList<HudPresetList.HudPresetEntry> {
        private final Minecraft mc;
        private final HudPresetSelectionScreen parent;
        private final List<File> files;

        public HudPresetList(Minecraft mc, HudPresetSelectionScreen parent, List<File> files) {
            super(mc, parent.width, parent.height, 32, parent.height - 64, 20);
            this.mc = mc;
            this.parent = parent;
            this.files = files;

            for (File file : files) {
                this.addEntry(new HudPresetEntry(file, mc.font));
            }
        }

        public final class HudPresetEntry extends ObjectSelectionList.Entry<HudPresetEntry> {
            private final File file;
            private final Font font;
            private boolean Selected;

            public HudPresetEntry(File file, Font font) {
                this.file = file;
                this.font = font;
            }

            @Override
            public Component getNarration()
            {
                return Component.empty();
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                return false;
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick)
            {
                guiGraphics.drawString(font, file.getName(), width + 2, height + 2, 0xFFFFFF);
            }
        }
    }
}

