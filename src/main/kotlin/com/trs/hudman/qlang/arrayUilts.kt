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

@file:JvmName("arrayUtil")
@file:Suppress("NOTHING_TO_INLINE", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.trs.hudman.qlang

import java.util.*

val Number.arsize:Int
    inline get() {
        
        return this.toInt() - 1
    }

val String.rsize:Int
    inline get() {
        
        return this.length.arsize
    }


val <bi> Array<bi>.rsize:Int
    inline get() {
        
        return this.size.arsize
    }

val <bi> List<bi>.rsize:Int
    inline get() {
        
        return this.size.arsize
    }

val <bi> Vector<bi>.rsize:Int
    inline get() {
        
        return this.size.arsize
    }

inline fun <bi> Array<bi>.last(): bi
{
    
    return this[this.rsize]
}

fun <bi> Array<bi>.toVet():Vector<bi>
{
    val out = Vector<bi>()
    for(v in this)
    {
        out.add(v)
    }
    return out
}



/*inline infix fun <bi> Array<bi>.append(ad:Array<bi>)
{
    val f = this.toVet()
    for (v in ad)
    {
        f.add(v)
    }
    var ou = Array<bi>(f.size)
    {
        return@Array Any() as bi
    }
    for (v in f.toArray())
    {
        ou + v
    }
    return ou
}*/

inline fun <bi> Array<bi>.first(): bi
{
    
    return this[0]
}

inline fun <bi> List<bi>.last(): bi
{
    
    return this[this.rsize]
}

inline fun <bi> List<bi>.first(): bi
{
    
    return this[0]
}

inline fun String.last(): Char
{
    
    return this[this.rsize]
}

inline fun String.first(): Char
{
    
    return this[0]
}

inline fun <bi> Vector<bi>.last(): bi
{
    
    return this[this.rsize]
}

inline fun <bi> Vector<bi>.first(): bi
{
    
    return this[0]
}

inline fun <bi> Vector<bi>.toStack() = this as Stack<bi>
inline fun <bi> Stack<bi>.toVector() = this as Vector<bi>