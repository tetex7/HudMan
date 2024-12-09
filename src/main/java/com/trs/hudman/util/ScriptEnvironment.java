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

import com.trs.hudman.gui.hudmods.AbstractHudElement;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.luaj.vm2.*;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

public class ScriptEnvironment
{
    public final String scriptText;

    public static Globals se_standardGlobals() {
        Globals globals = new Globals();
        /*globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new JseMathLib());
        globals.load(new LuajavaLib());*/
        LoadState.install(globals);
        LuaC.install(globals);
        return globals;
    }

    Globals globals = se_standardGlobals();

    private static LuaFunction getFunction(String name, Globals globals)
    {
        LuaValue v = globals.get(name);
        if (v == LuaValue.NIL)
        {
            throw new RuntimeException("Function " + name + " Does not exist");
        }
        else if (v.type() != LuaValue.TFUNCTION)
        {
            throw new RuntimeException( name + " Is not a function");
        }
        return (LuaFunction)v;
    }

    private final LuaFunction render;
    private final LuaFunction tick;
    private final LuaFunction init;

    public void registerElementThis(AbstractHudElement element)
    {
        exposeValue("this", element);
    }

    public void exposeValue(String name, Object obj)
    {
        globals.set(name, CoerceJavaToLua.coerce(obj));
    }

    public void callRender(float delta, GuiGraphics guiGraphics, Gui gui)
    {
        render.call(LuaValue.valueOf(delta), CoerceJavaToLua.coerce(guiGraphics), CoerceJavaToLua.coerce(gui));
    }

    public void callTick()
    {
        tick.call();
    }
    public void callInit()
    {
        init.call();
    }

    public ScriptEnvironment(String scriptText)
    {
        this.scriptText = scriptText;
        globals.load(scriptText).call();
        this.render = getFunction("render", globals);
        this.tick = getFunction("tick", globals);
        this.init = getFunction("init", globals);
    }
}
