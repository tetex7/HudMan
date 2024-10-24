package com.trs.hudman.mixin;

import com.trs.hudman.HudState;
import com.trs.hudman.gui.hudmods.HudElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Stack;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class PlayerGuiMixin
{
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderHotbar", at = @At("RETURN"))
    private void injectRenderHotbar(float pPartialTick, GuiGraphics pGuiGraphics, CallbackInfo info)
    {
        if (minecraft.getCameraEntity() != null)
        {
            if (HudState.getShowHud())
            {
                Stack<HudElement> huds = HudState.getHudElements();

                if (!huds.isEmpty())
                {
                    for (HudElement element : huds)
                    {
                        element.render(pPartialTick, pGuiGraphics, (Gui) (Object) this);
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
                Stack<HudElement> huds = HudState.getHudElements();

                if (!huds.isEmpty())
                {
                    for (HudElement hud : huds)
                    {
                        hud.tick();
                    }
                }
            }
        }
    }
}
