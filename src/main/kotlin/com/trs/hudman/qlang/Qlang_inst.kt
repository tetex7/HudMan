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

class Qlang_inst(block: Qlang_inst.(tag: String, p: Pattern, ctxt: String) -> String, p:Regex)
{
    private var __work: Qlang_inst.(tag:String, p:Pattern, ctxt: String)->String = block
    val regex:Regex = p
    fun rep(txt:String):String
    {
        return ""
    }

    /**
     *
     * @return the output string an the #1
     */
    fun work(tag:String, p:Pattern, ctxt: String):Pair<String, Int>
    {
        return Pair(__work(tag, p, ctxt), 1)
    }
    constructor(block: Qlang_inst.(tag: String, p: Pattern, ctxt: String) -> String) : this(block, Regex("not dep"))
}