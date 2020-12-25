package `2019`

import TemplateDay

class Day2 : TemplateDay(2, 2019) {

    var opCodeList : MutableList<Int>? = null
    init {
        opCodeList = input!!
            .joinToString("")
            .split(",")
            .map { i -> i.toInt() }
            .toMutableList()
    }

    override fun part1() {

        var opCodeList2 = opCodeList?.toMutableList()
        opCodeList2!![1] = 12
        opCodeList2!![2] = 2

        interpretOpCodeProgram(opCodeList2)

        println("Result is ${opCodeList2[0]}")
    }

    private fun interpretOpCodeProgram(opCodeList: MutableList<Int>) {
        var opCodeIndex = 0
        while (true) {
            val opCode = opCodeList[opCodeIndex]
            when (opCode) {
                1 -> {
                    val v1 = opCodeList[opCodeList[opCodeIndex + 1]]
                    val v2 = opCodeList[opCodeList[opCodeIndex + 2]]
                    opCodeList[opCodeList[opCodeIndex + 3]] = v1 + v2
                }
                2 -> {
                    val v1 = opCodeList[opCodeList[opCodeIndex + 1]]
                    val v2 = opCodeList[opCodeList[opCodeIndex + 2]]
                    opCodeList[opCodeList[opCodeIndex + 3]] = v1 * v2
                }
                99 -> {
                    break
                }
            }
            opCodeIndex += 4
        }
    }

    override fun part2() {

        for (noun in 0..99){
            for (verb in 0..99){
                var opList = opCodeList?.toMutableList()
                opList!![1] = noun
                opList!![2] = verb
                interpretOpCodeProgram(opList)
                if (opList[0] == 19690720) {
                    var result = 100*noun+verb
                    println("Result Part 2 is $result")
                    break;
                }
            }
        }
    }
}

fun main (args : Array<String>){
    Day2().run { part1(); part2() }
}