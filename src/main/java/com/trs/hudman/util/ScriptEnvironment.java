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

import net.minecraft.client.gui.GuiGraphics;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class ScriptEnvironment
{
    public final String scriptText;
    Globals globals = JsePlatform.standardGlobals();

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

    public void callRender(GuiGraphics guiGraphics, float delta)
    {
        render.call(new LuaUserdata(guiGraphics), LuaValue.valueOf(delta));
    }

    public void callTick()
    {
        tick.call();
    }

    public ScriptEnvironment(String scriptText)
    {
        this.scriptText = scriptText;
        globals.load(scriptText).call();
        this.render = getFunction("render", globals);
        this.tick = getFunction("tick", globals);
    }
}
