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

import com.mojang.blaze3d.vertex.PoseStack;
import com.trs.hudman.confg.JsonConfigHudElement;
import com.trs.hudman.util.ColorRGB;
import com.trs.hudman.util.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

/**
 * The super class for all HUD elements
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractHudElement extends HudCommonEnvironment implements IElementRenderPrimitive
{
    private final AbstractHudElement root;
    private final LocalPlayer player;
    private final Vec2i cords;
    private final Minecraft client;
    private final JsonConfigHudElement jsonElement;
    private final UUID elementUUID = UUID.randomUUID();
    private final Properties stringsProperties;
    private final ColorRGB confColor;
    private final boolean elementDebugMode;

    /**
     *
     * @param root Mostly time it's null and will probably be removed
     * @param client The current Minecraft client
     * @param cords The coordinates of the element on the user screen
     * @param jsonElement The Json config structure turned into a Java class
     * @implSpec Your constructor using this super class must contain all four
     */
    public AbstractHudElement(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i cords, @NotNull JsonConfigHudElement jsonElement)
    {
        this.cords = Objects.requireNonNull(cords);
        this.client = Objects.requireNonNull(client);
        this.jsonElement = Objects.requireNonNull(jsonElement);
        this.root = root;
        this.player = client.player;
        this.stringsProperties = new Properties();
        try
        {
            stringsProperties.load(new StringReader(String.join("\n", jsonElement.strings())));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        if (this.getStringsProperties().containsKey("bDebug"))
        {
            this.elementDebugMode = getStringOptionAs("bDebug", Boolean::parseBoolean);
        }
        else
        {
            this.elementDebugMode = false;
        }


        if (this.getStringsProperties().containsKey("iColor") && getStringOption("iColor").startsWith("0x"))
        {
            this.confColor = ColorRGB.ofInt(
                    Integer.parseInt(
                            getStringOption("iColor")
                                    .replace("0x", ""),
                            16
                    )
            );
        }
        else
        {
            this.confColor = ColorRGB.WHITE;
        }
    }

    /**
     * @return Provides a reference to the client
     */
    protected final Minecraft getClient()
    {
        return this.client;
    }

    /**
     * @return Provides a reference to the Json config Element
     * @see JsonConfigHudElement
     */
    public final JsonConfigHudElement getJsonElement()
    {
        return this.jsonElement;
    }

    /**
     * Irrelevant do not use<br>
     * PS. This was originally so you could nest an element inside of one another<br>
     * but as I built out the config system that idea kind of died
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    public final AbstractHudElement getRoot()
    {
        return this.root;
    }

    /**
     * @return Provides information about the current user player
     */
    public final LocalPlayer getPlayer()
    {
        return this.player;
    }

    /**
     * @return Coordinates where the element is to be rendered on the user screen
     */
    public final Vec2i getCords()
    {
        return this.cords;
    }

    /**
     * UUID is used for element identification
     * @return Provides the elements UID
     */
    public final UUID getElementUUID()
    {
        return elementUUID;
    }

    /**
     * Just in case you need access to the pre parsed properties
     * @return A reference to the auto Parsed string properties form {@link JsonConfigHudElement#strings()}
     */
    protected final Properties getStringsProperties()
    {
        return stringsProperties;
    }

    /**
     * @return Returns the {@code bDebug} value if present or is not it's false
     */
    public final boolean isElementDebugMode()
    {
        return elementDebugMode;
    }

    /**
     * @return Return the scale value provided by the configuration
     */
    public final float getScale()
    {
        return getJsonElement().scale();
    }

    /**
     * @return Returns a rudimentary RGB value from the {@code iColor} key
     */
    public final ColorRGB getConfColor()
    {
        return confColor;
    }

    /**
     * an easier wrapper around the {@link Properties} parser
     * {@snippet :
     * // An example of how to use this function call
     *     int testInt = getStringOptionAs("iTestInt", Integer::parseInt);
     * }
     * But do not use this on {@link String}s It's a waste of performance<br>
     * Use {@link AbstractHudElement#getStringOption(String)} instead
     *
     * @param key The name of the option you wish to parse
     * @param parserCall The string parser to return a valid type e.g. {@link Boolean#parseBoolean(String)}
     * @return A parsed version of the Option value string
     * @param <T> The parser output type
     */
    protected final <T> T getStringOptionAs(String key, @NotNull StringOptionParser<T> parserCall)
    {
        return Objects.requireNonNull(parserCall).parse((String)getStringsProperties().get(key));
    }

    /**
     * A easy wrapper around the {@link Properties} system
     * @param key the name of the option you wish to parse
     * @return The raw value string
     */
    protected final boolean hasStringOption(String key)
    {
        return this.getStringsProperties().containsKey(key);
    }

    /**
     * Returns the raw string value corresponding to the key
     * @param key The name of the option you wish to get
     * @return Returns the raw string value
     */
    protected final String getStringOption(String key)
    {
        return (String)getStringsProperties().get(key);
    }

    /**
     * Where all your graphical code lies for your element
     * @param partialTick The delta between ticks (I really don't know, and I've really never used this)
     * @param guiGraphics Minecraft's facilities to render to the screen
     * @param gui a reference to the player GUI
     */
    public abstract void render(float partialTick, GuiGraphics guiGraphics, Gui gui);

    /**
     * A place for your elements logic
     * @apiNote This function is run every gui tick and can weigh down the players client if you don't know what you're doing
     */
    public abstract void tick();

    /**
     * This function was made to address a bug involving higher than one scaling factors messing up coordinates<br>
     * Due to lack of translation<br>
     * Please perform the appropriate {@link PoseStack} push and pop<br>
     * see known_bugs.txt(B01)
     * @param poseStack A reference to the {@link GuiGraphics} drawing contexts as {@link PoseStack}
     */
    protected void doProperElementScaling(@NotNull PoseStack poseStack)
    {
        poseStack.translate(getCords().x(), getCords().y(), 0);
        poseStack.scale(getScale(), getScale(), getScale());
    }

    /**
     * This function was made to address a bug involving higher than one scaling factors messing up coordinates<br>
     * Due to lack of translation<br>
     * Please perform the appropriate {@link PoseStack} push and pop<br>
     * see known_bugs.txt(B01)
     * @param guiGraphics A reference to the {@link GuiGraphics} drawing contexts
     */
    protected void doProperElementScaling(@NotNull GuiGraphics guiGraphics)
    {
        doProperElementScaling(guiGraphics.pose());
    }

    /**
     * Performs a {@link PoseStack#pushPose()} then run your lambda Once done runs {@link PoseStack#popPose()}<br>
     * This is done to help with remitting a known bug with scaling factors higher than one<br>
     * see known_bugs.txt(B01) for more info
     * @param guiGraphics A reference to the current gui context
     * @param environment A lambda of code you wish to have run within the safe environment
     */
    void guiPoseStackSafeEnvironment(@NotNull GuiGraphics guiGraphics, @NotNull Runnable environment)
    {
        guiGraphics.pose().pushPose();
        environment.run();
        guiGraphics.pose().popPose();
    }

    /**
     * A full remedy to a known bug with scaling factors higher than one<br>
     * see known_bugs.txt(B01) for more info
     * @param guiGraphics A reference to the current gui context
     * @param environment A lambda of code you wish to have run within the scale safe environment
     */
    void doScaleSafeEnvironment(@NotNull GuiGraphics guiGraphics, @NotNull Runnable environment)
    {
        guiGraphics.pose().pushPose();
        doProperElementScaling(guiGraphics);
        environment.run();
        guiGraphics.pose().popPose();
    }

    /**
     * This exists To provide a lambda pattern for parser functions
     * As you can ignore this by instead of using {@link AbstractHudElement#getStringOptionAs(String, StringOptionParser)} you can use instead {@link AbstractHudElement#getStringOption(String)}
     */
    @FunctionalInterface
    protected interface StringOptionParser<T>
    {
        T parse(String text);
    }
}
