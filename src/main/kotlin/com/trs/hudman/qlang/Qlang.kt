@file:Suppress("NOTHING_TO_INLINE", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
package com.trs.hudman.qlang



import com.trs.hudman.qlang.libstd.h_day
import com.trs.hudman.qlang.libstd.h_hour
import com.trs.hudman.qlang.libstd.h_minute
import com.trs.hudman.qlang.libstd.h_month
import com.trs.hudman.qlang.libstd.h_second
import com.trs.hudman.qlang.libstd.h_year
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random
import java.util.*
import java.util.regex.Pattern

typealias FLAG = Boolean

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

class Qlang
{
    private val pats: Stack<Pair<Regex, Qlang_inst>> = Stack()
    //private val


    companion object
    {
        fun Builder() = qlangBuilder()
        const val q_NAT = "&NAT&"
        const val q_rand = "%rand%"
        const val rq_randR = "%rand[+--][0-9]*%"
        const val r_rand = "%rand[0-9]*%"
        const val q_randV = "%rand-[1-9]*-[1-9]*%"
        const val q_ntap = "%ntap%"
        const val q_nntap = "%nntap%"
       // private const val qps_rtc2 = "%*[a-zA-Z_]*%*"
        private const val qps_rtc = "%*[a-zA-Z_]-*%*"
        val qp_rtc = Pattern.compile(qps_rtc)
        //val qp_rtc2 = Pattern.compile(qps_rtc2)

    }

    val tok = '%'

    init
    {
        /*this[Regex(rq_randR)] = RET@{ tag:String, p:Pattern, ctxt:String ->
            val Rv = tag.replace(qp_rtc.toRegex(), "")
            var v = if (Rv.toInt() == 0) 0 else Rv.toInt()
            /*println(tag)
            println(Rv)
            println(v)*/

            return@RET Random.nextInt(1221+v, 9119+v).toString()
        }*/


        /*this[Regex(r_rand)] = RET@{ tag:String, p:Pattern, ctxt:String ->
            val Rv = tag.replace(qp_rtc.toRegex(), "")
            var v = if (Rv.toInt() == 0) 0 else Rv.toInt()
            /*println(tag)
            println(Rv)
            println(v)*/

            return@RET Random.nextInt(1221-v, 9119+v).toString()
        }*/

        this[Regex(q_randV)] = RET@{ tag:String, p:Pattern, ctxt:String ->
            val Rv = tag.replace(qp_rtc.toRegex(), "")
            val Sv = Rv.split("-")
            val F = if (Sv[0].toInt() == 0) 0 else Sv[0].toInt()
            val T = if (Sv[1].toInt() == 0) 0 else Sv[1].toInt()
            /*println(tag)
            println(Rv)
            println(v)*/


            return@RET Random.nextInt(F, T).toString()
        }
        this[q_rand] = RET@{ tag:String, p:Pattern, ctxt:String -> Random.nextInt(1991, 9119).toString()}
        this[q_ntap] = { tag:String, p:Pattern, ctxt:String -> "\t\n" }
        this[q_nntap] = { tag:String, p:Pattern, ctxt:String -> "\n\t" }
    }

    private var is_std:FLAG = false

    fun initLibSTD()
    {
        if (!is_std)
        {
            this[h_hour] = { tag: String, p: Pattern, ctxt: String -> LocalTime.now().hour.toSFstring() }
            this[h_minute] = { tag: String, p: Pattern, ctxt: String -> LocalTime.now().minute.toSFstring() }
            this[h_second] = { tag: String, p: Pattern, ctxt: String -> LocalTime.now().second.toSFstring() }
            this[h_month] = { tag: String, p: Pattern, ctxt: String -> LocalDate.now().month.value.toSFstring() }
            this[h_day] = { tag: String, p: Pattern, ctxt: String -> LocalDate.now().dayOfMonth.toSFstring() }
            this[h_year] = { tag: String, p: Pattern, ctxt: String -> LocalDate.now().year.toSFstring() }
            this[q_NAT] = { tag: String, p: Pattern, ctxt: String -> "NAT" }
        }
    }

    inline operator fun invoke(txt:String):Pair<String, FLAG>
    {
        return pars(txt)
    }

    /**
     * @param text input string
     *
     */
    fun pars(text:String):Pair<String, FLAG>
    {
        var workd = 0 // the num work of done the string
        var rou = text //to tun val text to var
        for (i in 0 .. pats.size.arsize) //a for loop to go thow the Vector of tags
        {
            val ib = pats[i]
            if(rou.contains(ib.first)) //check if the input contains a tag
            {

                val t = ib.first.find(text)?.value ?: q_NAT //prep tag for the work fun
                rou = rou.replace(ib.first, /*a logic block for the replace*/run RET@{
                    val v = ib.second.work(/*prep tag*/t, /*the tag's regx Pattern*/ib.first.toPattern(), /*input str*/rou)
                    workd += v.second //add the #1 to workd
                    return@RET v.first //the replace str
                })
            }

        }

        val of:FLAG = workd > 0 //if work is done
        return Pair(rou, of) // the output of a str and FLAG
    }

    operator fun set(regex_p: String, v: Qlang_inst)
    {
        var pp_regex_p = regex_p

        pats.add(Pair(Regex(pp_regex_p), v))
    }

    operator fun set(regex_p: String, str:String)
    {
        var pp_regex_p = regex_p
        val fv: Qlang_inst.(tag:String, p:Pattern, ctxt:String)->String = { tag:String, p:Pattern, ctxt:String -> str }

        pats.add(Pair(Regex(pp_regex_p), Qlang_inst(fv, Regex(pp_regex_p))))
    }

    operator fun set(regex: Regex, str:String)
    {
        val pp_regex_p = regex
        val fv: Qlang_inst.(tag:String, p:Pattern, ctxt:String)->String = { tag:String, p:Pattern, ctxt:String -> str }
        pats.add(Pair(pp_regex_p, Qlang_inst(fv, regex)))
    }

    operator fun set(regex_p: String, fv: Qlang_inst.(tag:String, p:Pattern, ctxt:String)->String)
    {
        var pp_regex_p = regex_p
        pats.add(Pair(Regex(pp_regex_p, ), Qlang_inst(fv, Regex(pp_regex_p))))
    }


    operator fun set(regex: Regex, v: Qlang_inst)
    {
        val pp_regex_p = regex
        pats.add(Pair(pp_regex_p, v))
    }

    operator fun set(regex: Regex, fv: Qlang_inst.(tag:String, p:Pattern, ctxt:String)->String)
    {
        val pp_regex_p = regex
        pats.add(Pair(pp_regex_p, Qlang_inst(fv, regex)))
    }
}