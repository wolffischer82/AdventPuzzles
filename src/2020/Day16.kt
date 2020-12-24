package `2020`

import readFile

class Day16 (filename : String) {
    val filename = filename

    enum class InputState {
        Rules,
        YourTicket,
        NearbyTicket
    }

    val ruleRangesMap = mutableMapOf<String, MutableList<Pair<Int,Int>>>();
    val nearbyTicketList = mutableListOf<List<Int>>()
    var myTicket = listOf<Int>()

    fun part1(){

        val input = readFile(filename)
        var inputState = InputState.Rules
        var wrongValues = 0;
        var ruleCounter = 0;

        for (i in input){
            if (i == "") continue
            if (i.startsWith("your ticket")) {
                inputState = InputState.YourTicket
            } else if (i.startsWith("nearby tickets")) {
                inputState = InputState.NearbyTicket
            }
            else {
                when (inputState) {
                    InputState.Rules -> {
                        val splitInput = i.split(": ")
                        val pairStrings = splitInput[1].split(" or ")

                        for (p in pairStrings) {
                            val pairValuesString = p.split("-")
                            ruleRangesMap.putIfAbsent(splitInput[0], mutableListOf());
                            ruleRangesMap[splitInput[0]]!!.add(
                                Pair(
                                    pairValuesString[0].toInt(),
                                    pairValuesString[1].toInt()
                                )
                            )
                        }
                    }

                    InputState.YourTicket -> {
                        myTicket = i.split(",").map { i -> i.toInt() }
                    }

                    InputState.NearbyTicket -> {
                        ruleCounter++
                        val splitInput = i.split(",")
                        var correctRule = true;
ruleCheck@              for (v in splitInput) {

                            var correctValue = false;
                            var vInt = v.toInt()
                            for (pairList in ruleRangesMap.values) {
                                for (p in pairList) {
                                    if (vInt >= p.first && vInt <= p.second) {
                                        correctValue = true;
                                    }
                                }
                            }
                            if (!correctValue) {
                                correctRule = false;
                                wrongValues += vInt
                                break@ruleCheck
                            }
                        }

                        if (correctRule){
                            nearbyTicketList.add(i.split(",").map { i -> i.toInt() })
                        }

                    }
                }
            }
        }
        println("Wrong values are $wrongValues")
        println("Total number of correct rules is ${nearbyTicketList.size} out of $ruleCounter")
    }

    fun part2(){
        val indexRuleMap = mutableMapOf<Int, MutableList<String>>()

        for (i in 0 .. nearbyTicketList[0].size-1) {
            indexRuleMap[i] = ruleRangesMap.keys.toMutableList()
        }

        //Filter the rules per index
        for (t in nearbyTicketList){
            for ((i,v) in t.withIndex()){
                val removeRuleList = mutableListOf<String>()
                for (rStr in indexRuleMap[i]!!){
                    val p1 = ruleRangesMap[rStr]!![0]
                    val p2 = ruleRangesMap[rStr]!![1]
                    if (!(v >= p1.first && v <= p1.second) &&
                            !(v >= p2.first && v <= p2.second)) {
                                removeRuleList.add(rStr);
                    }
                }
                indexRuleMap[i]!!.removeAll(removeRuleList)
            }
        }

        val ruleList = indexRuleMap.values.toMutableList().apply { sortByDescending { it.size }}

        for (i in 0..ruleList.size-2) {
            val v1 = ruleList[i]
            val v2 = ruleList[i+1]
            v1.removeAll(v2)
        }

        var result = 1L
        for ((i,v) in indexRuleMap) {
            if (v[0].startsWith("departure")){
                result *= myTicket[i].toLong()
            }
        }

        println("My Departure value is $result")
    }
}
fun main(args : Array<String>){
    Day16("data/2020/Day16Input").run { part1(); part2() }

}