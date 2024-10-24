package com.trs.hudman.confg;

import io.github.cottonmc.cotton.gui.widget.data.Vec2i;

public record JsonConfgHudElement(
        String ElementId,
        Vec2i Cords,
        int Width,
        int Height,
        //int NodeId,
        //int pairMode,w
        String pairGameHudElement,
        Boolean enable,
        String[] Strs

)
{
    public static JsonConfgHudElement  Copy(JsonConfgHudElement element)
    {
        JsonConfgHudElement out = new JsonConfgHudElement(
                element.ElementId,
                element.Cords,
                element.Width,
                element.Height,
                element.pairGameHudElement,
                element.enable,
                element.Strs
        );

        return out;
    }
}
