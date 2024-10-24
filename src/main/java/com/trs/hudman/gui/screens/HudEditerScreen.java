package com.trs.hudman.gui.screens;

import java.util.function.Supplier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class HudEditerScreen extends Screen
{
    //private final Screen lastScreen;
    private static final Component TestText = Component.literal("Test");
    private Button Test;

    public HudEditerScreen(Component title)
    {
        super(title);
        //lastScreen = LastScreen;
    }


    public void tick()
    {
        super.tick();
    }

    @Override
    protected void init()
    {
        Test = this.addRenderableWidget(Button.builder(Component.translatable("gui.ok"), (pButton)-> {
            this.onClose();
        }).build());
        super.init();

    }

    void saveHud()
    {

    }

    private void renderPngBackground(GuiGraphics pGuiGraphics)
    {
        ResourceLocation HUDMAN_BACKGRPOUND = ResourceLocation.tryBuild("minecraft", "textures/block/deepslate.png");
        if (HUDMAN_BACKGRPOUND == null)
        {
            HUDMAN_BACKGRPOUND = BACKGROUND_LOCATION;
        }
        pGuiGraphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
        pGuiGraphics.blit(HUDMAN_BACKGRPOUND, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 32, 32);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
    {
        //this.renderDirtBackground(pGuiGraphics);
        //this.renderPngBackground(pGuiGraphics);
        this.renderBackground(pGuiGraphics);
        pGuiGraphics.drawString(this.font, TestText, this.width / 2 - 100, 53, 10526880);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.Test.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
