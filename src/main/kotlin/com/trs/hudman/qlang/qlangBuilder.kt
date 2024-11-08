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

package com.trs.hudman.qlang

import java.util.regex.Pattern

class qlangBuilder
{
    val ql = Qlang()
    fun add(tag:String, ini: Qlang_inst.(tag:String, p: Pattern, ctxt:String)->String): qlangBuilder
    {
        ql[Regex(tag)] = Qlang_inst(ini, Regex(tag))
        return this
    }

    fun add(tag:String, ini: Qlang_inst): qlangBuilder
    {
        ql[Regex(tag)] = ini
        return this
    }

    fun initLibSTD(): qlangBuilder
    {
        ql.initLibSTD()
        return this
    }

    fun bulid(init_std:FLAG = true): Qlang
    {
        if (init_std)
        {
            initLibSTD()
        }
        return ql;
    }

}