class RuleNode(id : String) {
    val id = id
    val childVariations = mutableSetOf<List<RuleNode>>()
    var literal = ""

    fun matchesRule(message : String) : Boolean {
        return matchesRule(message, 0) == message.length
    }
    private fun matchesRule(message: String, index : Int) : Int {
        if (index >= message.length) return -1

        if (literal != ""){
            val charStr = message[index].toString()
            if (charStr == literal){
                return index+1
            }
            return -1
        }

varC@   for (variation in childVariations){
            var lastIndex = index

            for (c in variation){
                lastIndex = c.matchesRule(message, lastIndex)
                if (lastIndex < 0) {
                    continue@varC
                }
            }
            if (lastIndex >= 0) {
                return lastIndex
            }
        }
        return -1
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
    Day19("src/Day19Input").run { part1(); part2() }
}