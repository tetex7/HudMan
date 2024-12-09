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

package com.trs.hudman.util.scripting;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class WidgetLib extends TwoArgFunction
{
    @Override
    public LuaValue call(LuaValue modname, LuaValue env)
    {
        LuaValue library = tableOf();
        library.set("getWidget", new getWidgetFunction());
        env.set("widgetLib", library);
        env.get("package").get("loaded").set("widgetLib", library);
        return library;
    }



    static class getWidgetFunction  extends OneArgFunction
    {
        static final String pac = "com.trs.hudman.gui.hudmods.widget";

        protected static Class<?> classForName(String name) throws ClassNotFoundException
        {
            return Class.forName(name, true, ClassLoader.getSystemClassLoader());
        }

        public LuaValue call(LuaValue name)
        {
            if (name.isstring())
            {
                try
                {
                    Class<?> czz = classForName(pac + '.' + name.tojstring());
                    return CoerceJavaToLua.coerce(czz);
                } catch (ClassNotFoundException e)
                {
                    return LuaValue.NIL;
                }
            }
            return LuaValue.NIL;
        }
    }
}
