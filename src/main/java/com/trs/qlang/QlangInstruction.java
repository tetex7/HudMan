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

package com.trs.qlang;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class QlangInstruction
{
    public static QlangInstruction of(InstructionWork code)
    {
        return new QlangInstruction(code);
    }

    @FunctionalInterface
    public interface InstructionWork
    {
        String work(String tag, Pattern pattern);
    }

    protected final InstructionWork getCode()
    {
        return code;
    }

    private final InstructionWork code;

    public String run(String tag, Pattern pattern, String ctxt)
    {
        if (code == null)
        {
            return null;
        }
        return code.work(tag, pattern);
    }

    public QlangInstruction(InstructionWork code)
    {
        this.code = code;
    }

    @Override
    public int hashCode()
    {
        return ThreadLocalRandom.current().nextInt();
    }
}
