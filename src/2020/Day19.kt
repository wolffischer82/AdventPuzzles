package `2020`

import readFile

class RuleNode(id : String) {
    val id = id
    val childVariations = mutableSetOf<List<RuleNode>>()
    var literal = ""

    fun matchesRule(message : String) : Boolean {
        return matchesRule(message, 0).contains(message.length)
    }
    private fun matchesRule(message: String, index : Int) : List<Int> {
        if (index >= message.length) return listOf()

        if (literal != ""){
            val charStr = message[index].toString()
            if (charStr == literal){
                return listOf(index+1)
            }
            return listOf()
        }

        var result = mutableListOf<Int>()

varC@   for (variation in childVariations){

            var lastResult = mutableListOf<Int>().apply { add(index) }
            var newResult = mutableListOf<Int>()
            for (c in variation){
                for (i in lastResult){
                    newResult.addAll(c.matchesRule(message, i))
                }
                lastResult.clear()
                lastResult.addAll(newResult)
                newResult.clear()
            }
            result.addAll(lastResult)
        }

        return result
    }

    fun addChildVariation(nodeList : List<RuleNode>){
        childVariations.add(nodeList)
    }
}

class Day19 (filename : String) {
    val filename = filename
    val ruleMap = mutableMapOf<String, MutableSet<MutableList<String>>>()

    fun part1(){
        val input = readFile(filename)
        val inputList = mutableListOf<String>()
        for (i in input){
            val splitInput = i.split(": ")
            if (splitInput.size > 1){
                ruleMap.putIfAbsent(splitInput[0], mutableSetOf())
                for (r in splitInput[1].split(" | ")){
                    val idList = mutableListOf<String>()
                    for (id in r.split(" ")){
                        idList.add(id)
                    }
                    ruleMap[splitInput[0]]?.add(idList)
                }
            }
            else{
                inputList.add(splitInput[0])
            }
        }

        val ruleNodeMap = mutableMapOf<String, RuleNode>()
        for (k in ruleMap.keys){
            ruleNodeMap.putIfAbsent(k, RuleNode(k))
        }
        for ((k,idListSet) in ruleMap){
            val node = ruleNodeMap[k]
            for (idList in idListSet){
                val nodeListTemp = mutableListOf<RuleNode>()
                for (r in idList) {
                    if (ruleNodeMap.containsKey(r)){
                        val rNode = ruleNodeMap[r]
                        nodeListTemp.add(rNode!!)
                    }
                    else {
                        node!!.literal = r[1].toString()
                    }
                }
                if (nodeListTemp.size > 0){
                    node!!.addChildVariation(nodeListTemp)
                }
            }
        }

        var result = 0
        var firstNode = ruleNodeMap["0"]
        for (i in inputList){
            if (firstNode!!.matchesRule(i)){
                result++
                println("$i is a correct rule!")
            }
        }
        println("Correct rules are $result")
    }
    fun part2(){

    }
}

fun main(args : Array<String>){
    Day19("data/2020/Day19Input").run { part1() }
    Day19("data/2020/Day19InputPart2").run { part1() }
}