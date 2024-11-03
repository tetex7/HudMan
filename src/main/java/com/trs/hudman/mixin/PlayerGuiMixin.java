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

package com.trs.hudman.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.trs.hudman.HudState;
import com.trs.hudman.gui.hudmods.AbstractHudElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Stack;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class PlayerGuiMixin
{
    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private PlayerTabOverlay tabList;

    @Inject(method = "renderHotbar", at = @At("RETURN"))
    private void injectRenderHotbar(float partialTick, GuiGraphics guiGraphics, CallbackInfo info)
    {
        if (minecraft.getCameraEntity() != null)
        {
            if (HudState.getShowHud())
            {
                Stack<AbstractHudElement> huds = HudState.getHudElements();
                if (!huds.isEmpty())
                {

                    for (AbstractHudElement element : huds)
                    {
                        String pairGameHudElement = element.getJsonElement().pairGameHudElement();
                        if (pairGameHudElement.equals(HudState.getGameHudElements().get("hotbar").toString()) || pairGameHudElement.equals(""))
                        {
                            element.render(partialTick, guiGraphics, (Gui)(Object)this);
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "renderEffects", at = @At("RETURN"))
    private void injectRenderEffects(GuiGraphics guiGraphics, CallbackInfo ci, @Local Collection<MobEffectInstance> collection)
    {
        if (!collection.isEmpty())
        {
            if (HudState.getShowHud())
            {
                Stack<AbstractHudElement> huds = HudState.getHudElements();
                if (!huds.isEmpty())
                {
                    for (AbstractHudElement element : huds)
                    {
                        String pairGameHudElement = element.getJsonElement().pairGameHudElement();
                        if (pairGameHudElement.equals(HudState.getGameHudElements().get("effectbar").toString()))
                        {
                            element.render(0, guiGraphics, (Gui)(Object)this);
                        }

                    }
                }
            }
        }
    }

    @Inject(method = "tick()V", at = @At("RETURN"))
    private void injectTick(CallbackInfo info)
    {
        if (minecraft.getCameraEntity() != null)
        {
            if (HudState.getShowHud())
            {
                Stack<AbstractHudElement> huds = HudState.getHudElements();

                if (!huds.isEmpty())
                {
                    for (AbstractHudElement hud : huds)
                    {
                        hud.tick();
                    }
                }
            }
        }
    }
}
