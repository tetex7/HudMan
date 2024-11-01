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

@file:JvmName("typeUtils")

package com.trs.x46FW.utils


//import com.trs.x46FW.DSS.wintest
//import com.trs.x46FW.tlang.ValData
import java.io.File
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.HashMap

import kotlin.math.abs


inline fun delay(millis:Long) = Thread.sleep(millis)

@Suppress("UNCHECKED_CAST")
fun <bi> dud():bi
{
    return null as bi
}


fun Number.toSFstring(): String
{
    return if(this.toInt() < 10) "0${this.toInt()}" else this.toInt().toString()
}
fun <bi, vet_obj : Vector<Pair<String, bi>>> vet_obj.seekf(str:String, offSet:Int = 0): Pair<String, bi>?
{
    for (i in offSet .. this.size)
    {
        if (this[i].first == str)
        {
            return this[i]
        }
    }
    return null
}

fun <bi, vet_obj : Vector<Pair<String, bi>>> vet_obj.seek(str:String, offSet:Int = 0): bi?
{
    for (i in offSet .. this.size)
    {
         if (this[i].first == str) return this[i].second
    }
    return null
}
fun Boolean.toStrY():String
{
    return if (this) "YES" else "NO"
}

fun Boolean.toStrOff():String
{
    return if(this) "NO" else "OFF"
}

fun SWITCH(value: Boolean):Boolean = !value

operator fun Boolean.inc():Boolean
{
    return SWITCH(this)
}

operator fun Boolean.dec():Boolean
{
    return SWITCH(this)
}

val Boolean.YorN:String
    get(){
        return toStrY()
    }

val Boolean.NorO:String
    inline get(){
        return toStrOff()
    }

fun Char.toSH(): Short
{
    return this.code.toShort()
}

fun Number.toBool(): Boolean
{
    val DAT = this.toInt()
    return (abs(DAT) <= 1)
}

fun String.toBool(): Boolean
{
    for (v in this)
    {
        if (!v.isDigit())
        {
            throw IllegalArgumentException("is")
        }
    }
    val DAT = (this).toInt()
    return DAT.toBool()
}


/*fun Boolean.toggle()
{
    val b = !this
    //this = false

}*/

/**
 * this is a infix form of [java.util.HashMap.remove] that has key of type [String]
 * @param k value type of the map
 */
infix fun <k> HashMap<String, k>.remov(name_s:String)
{

    this.remove(name_s)
}

fun <bi> Array<bi>.str(i: Int): String
{
    
    return this[i].toString()
}

fun Short.str(): String
{
    
    return this.toString()
}

fun <bi> Array<bi>.str(): String
{
    
    var ou = String()
    ou = "[ "
    var i = 0
    while (i < this.size)
    {
        if (i == (this.size - 1))
        {
            ou += "${this[i]} ]"
            break
        }
        else
        {
            ou += "${this[i]}, "
        }

        i++
    }
    return  ou
}

fun Array<Byte>.toUByte(): Array<UByte>
{
    
    val o:Array<UByte> = Array<UByte>(this.size)
    {
        0u
    }
    for (i in 0 .. this.size)
    {
        o[i] = this[i].toUByte()
    }
    return o
}

fun Array<UByte>.toByte(): Array<Byte>
{
    
    val o:Array<Byte> = Array<Byte>(this.size)
    {
        0
    }
    for (i in 0 .. this.size)
    {
        o[i] = this[i].toByte()
    }
    return o
}

operator fun Char.times(by:Number):String
{
    var str = ""
    for (i in 0..by.toLong())
    {
        str += this
    }
    return str
}

fun <bi> Array<bi>.toString(): String
{
    
    var ou = String()
    ou = "[ "
    //var i = 0
    for (i in 0 .. this.size)
    {
        if (i == (this.size - 1))
        {
            ou += "${this[i]} ]"
            break
        }
        else
        {
            ou += "${this[i]}, "
        }
    }
    return  ou
}