package com.trs.hudman.confg;

import com.trs.hudman.HudState;
import com.trs.hudman.gui.hudmods.*;
import com.trs.hudman.mixin.PlayerGuiMixinAccessor;
import com.trs.hudman.util.INamespaceHandler;
import com.trs.hudman.util.NameSpacePath;
import io.github.cottonmc.cotton.gui.widget.data.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;

import java.io.PrintWriter;
import java.io.StringWriter;

@Environment(EnvType.CLIENT)
public class ConfgHelper
{
    public static void mkHud(Minecraft minecraft)
    {
        JsonConfgHudFile huds = HudState.getCong();
        HudState.getHudElements().clear();
        for (JsonConfgHudElement element : huds.Elements())
        {
            if (element.enable())
            {

                NameSpacePath path = new NameSpacePath(element.ElementId());
                HudState.getLOGGER().info("New ElementID:'{}' to load", path);
                if (path.getNamespace().equals("hudman"))
                {
                    try
                    {
                        switch (path.getPath())
                        {
                            case "cords" -> HudState.getHudElements().push(new CordsElement(null, minecraft, element.Cords(), element));
                            case "text" -> HudState.getHudElements().push(new TextElement(null, minecraft, element.Cords(), element));
                            case "compass" -> HudState.getHudElements().push(new CompassElement(null, minecraft, element.Cords(), element));
                            case "velocity_vector" -> HudState.getHudElements().push(new VelocityVectorElement(null, minecraft, element.Cords(), element));
                            case "fps" -> HudState.getHudElements().push(new FPSElement(null, minecraft, element.Cords(), element));
                            default ->
                            {
                                HudState.getLOGGER().info("no element by Id:'{}'", element.ElementId());
                                continue;
                            }
                        }
                        HudState.getLOGGER().info("loaded ElementID:'{}'", path);
                    }
                    catch (Throwable exception)
                    {
                        String str_namespace_error = "§i§cError in built-in NamespaceHandler:'" + path.getNamespace() + "' on Loading ElementName:'" + path.getPath() + "'§r";
                        /*CrashReport crashReport = CrashReport.forThrowable(exception, str_namespace_error);
                        throw new ReportedException(crashReport);*/
                        JsonConfgHudElement json_element = new JsonConfgHudElement(
                                element.ElementId(),
                                new Vec2i(
                                        ((PlayerGuiMixinAccessor)minecraft.gui).GetScreenWidth() / 2,
                                        (((PlayerGuiMixinAccessor)minecraft.gui).GetScreenHeight() / 2) + 10
                                ),
                                element.Width(),
                                element.Height(),
                                element.pairGameHudElement(),
                                true,
                                new String[]{
                                        str_namespace_error,
                                }
                        );
                        HudState.getHudElements().push(new TextElement(null, minecraft, json_element.Cords(), json_element));
                        HudState.getLOGGER().error("Exception in built-in NamespaceHandler:'{}' on Loading ElementName:'{}'\n{}", path.getNamespace(), path.getPath(), stackTraceString(exception));
                    }
                }
                else
                {
                    INamespaceHandler name_fun = HudState.getNamespaceHandlers().get(path.getNamespace());
                    if (name_fun != null)
                    {
                        try
                        {
                            boolean ret_v = name_fun.work(element);
                            if (ret_v)
                            {
                                HudState.getLOGGER().warn("NamespaceHandler for Namespace:'{}' returned a bool of 1", path.getNamespace());
                            }
                        }
                        catch (Throwable exception)
                        {
                            String str_namespace_error = "§i§cError in Loading NamespaceHandler ID:'" + path.getNamespace() + "'§r";
                            JsonConfgHudElement json_element = new JsonConfgHudElement(
                                    element.ElementId(),
                                    new Vec2i(
                                            ((PlayerGuiMixinAccessor)minecraft.gui).GetScreenWidth() / 2,
                                            (((PlayerGuiMixinAccessor)minecraft.gui).GetScreenHeight() / 2) + 10
                                    ),
                                    element.Width(),
                                    element.Height(),
                                    element.pairGameHudElement(),
                                    true,
                                    new String[]{
                                            str_namespace_error,
                                    }
                            );
                            HudState.getHudElements().push(new TextElement(null, minecraft, json_element.Cords(), json_element));
                            HudState.getLOGGER().error("Exception in NamespaceHandler:'{}' on Loading ElementName:'{}'\n{}", path.getNamespace(), path.getPath(), stackTraceString(exception));
                        }
                    }
                    else
                    {
                        HudState.getLOGGER().warn("No NamespaceHandler for ElementID:'{}'", path.getFpath());
                    }
                    continue;
                }
            }
        }
    }

    public static String stackTraceString(Throwable throwable)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        throwable.printStackTrace(pw);

        pw.flush();
        return sw.toString();
    }
}
