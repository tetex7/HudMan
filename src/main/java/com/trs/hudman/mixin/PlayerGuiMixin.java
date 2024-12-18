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
import com.trs.hudman.gui.hudmods.AbstractHudElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.trs.hudman.HudState;

import java.util.Collection;
import java.util.Stack;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class PlayerGuiMixin
{
    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private PlayerTabOverlay tabList;

    @Shadow protected abstract Player getCameraPlayer();

    @Inject(method = "renderItemHotbar", at = @At("RETURN"))
    private void injectRenderHotbar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info)
    {
        if (this.getCameraPlayer() != null)
        {
            if (HudState.showHud)
            {
                //minecraft.getProfiler().push("HudMan Rendering Hotbar");
                Stack<AbstractHudElement> huds = HudState.hudElements;
                if (!huds.isEmpty())
                {

                    for (AbstractHudElement element : huds)
                    {
                        String pairGameHudElement = element.getJsonElement().pairGameHudElement();
                        if (pairGameHudElement.equals(HudState.gameHudElements.get("hotbar").toString()) || pairGameHudElement.isEmpty())
                        {
                            element.render(deltaTracker.getGameTimeDeltaPartialTick(false), guiGraphics, (Gui)(Object)this);
                        }
                    }
                }
                //minecraft.getProfiler().pop();
            }
        }
    }

    @Inject(method = "renderPlayerHealth", at = @At("RETURN"))
    private void injectRenderPlayerHealth(GuiGraphics guiGraphics, CallbackInfo info)
    {
        if (this.getCameraPlayer() != null)
        {
            if (HudState.showHud)
            {
                //minecraft.getProfiler().push("HudMan Rendering Hotbar");
                Stack<AbstractHudElement> huds = HudState.hudElements;
                if (!huds.isEmpty())
                {

                    for (AbstractHudElement element : huds)
                    {
                        String pairGameHudElement = element.getJsonElement().pairGameHudElement();
                        if (pairGameHudElement.equals(HudState.gameHudElements.get("healthbar").toString()))
                        {
                            element.render(0, guiGraphics, (Gui)(Object)this);
                        }
                    }
                }
                //minecraft.getProfiler().pop();
            }
        }
    }

    @Inject(method = "renderEffects", at = @At("RETURN"))
    private void injectRenderEffects(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info, @Local Collection<MobEffectInstance> collection)
    {
        if (!collection.isEmpty())
        {
            if (HudState.showHud)
            {
                //minecraft.getProfiler().push("HudMan Rendering Effectbar");
                Stack<AbstractHudElement> huds = HudState.hudElements;
                if (!huds.isEmpty())
                {
                    for (AbstractHudElement element : huds)
                    {
                        String pairGameHudElement = element.getJsonElement().pairGameHudElement();
                        if (pairGameHudElement.equals(HudState.gameHudElements.get("effectbar").toString()))
                        {
                            element.render(deltaTracker.getGameTimeDeltaPartialTick(false), guiGraphics, (Gui)(Object)this);
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "tick()V", at = @At("RETURN"))
    private void injectTick(CallbackInfo info)
    {
        if (this.getCameraPlayer() != null)
        {
            if (HudState.showHud)
            {
                //minecraft.getProfiler().push("HudMan Tick");
                Stack<AbstractHudElement> huds = HudState.hudElements;

                if (!huds.isEmpty())
                {
                    for (AbstractHudElement hud : huds)
                    {
                        hud.tick();
                    }
                }
                //minecraft.getProfiler().pop();
            }
        }
    }
}
