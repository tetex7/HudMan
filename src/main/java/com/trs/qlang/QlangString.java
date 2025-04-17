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

package com.trs.qlang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class QlangString implements CharSequence
{
    private final Qlang qInterpreter = Qlang.builder().includeStandardLibrary().build();
    private final String rawStr;
    
    public static @NotNull QlangString ofLiteral(String string)
    {
        return new QlangString(string);
    }

    protected QlangString(String text)
    {
        this.rawStr = text;
    }

    @Override
    public int length()
    {
        return toString().length();
    }

    @Override
    public char charAt(int index)
    {
        return toString().charAt(index);
    }

    @Override
    public @NotNull CharSequence subSequence(int start, int end)
    {
        return toString().subSequence(start, end);
    }

    public  Qlang.WorkOutput getQoutput()
    {
        return qInterpreter.parse(rawStr);
    }

    @Override
    public @NotNull String toString()
    {
        return getQoutput().outputString();
    }

    public final Qlang getQLangInterpreter()
    {
        return qInterpreter;
    }
}
