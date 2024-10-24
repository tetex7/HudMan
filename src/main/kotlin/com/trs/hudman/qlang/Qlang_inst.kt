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