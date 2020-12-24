package `2020`

import readFile

class Day15(filename : String) {
    val filename = filename

    fun part1(turnLimit : Int){
        val input = readFile(filename)[0].split(",").map { i -> i.toInt() }
        var lastNumber = input[input.size-1]
        val numberCounter = mutableMapOf<Int, Int>()
        val numberTurnMap = mutableMapOf<Int, MutableList<Int>>()

        for (i in 0..input.size-1) {
            numberCounter[input[i]] = 1
            numberTurnMap[input[i]] = mutableListOf<Int>().apply { add(i)}
        }

        for (turn in input.size until turnLimit){

            if (numberCounter.containsKey(lastNumber) && numberCounter[lastNumber] == 1) {
                lastNumber = 0;
            }else if (numberCounter.containsKey(lastNumber) && numberCounter[lastNumber]!! > 1) {
                val turns = numberTurnMap[lastNumber]!!
                val turn1 = turns[turns.size-1]
                val turn2 = turns[turns.size-2]
                lastNumber = turn1 - turn2
            }
            if (!numberCounter.containsKey(lastNumber)){
                numberTurnMap[lastNumber] = mutableListOf()
                numberCounter[lastNumber] = 0
            }

            numberCounter[lastNumber] = numberCounter[lastNumber]!! + 1
            numberTurnMap[lastNumber]!!.add(turn)
        }
        println("Last number is $lastNumber")
    }
}
fun main (args : Array<String>){
    Day15("data/2020/Day15Input").run { part1(2020); part1(30000000) }
}