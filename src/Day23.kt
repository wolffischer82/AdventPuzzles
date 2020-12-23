class Day23 (filename : String){
    val filename = filename

    fun findDestinationIndex(index : Int, cups : List<Int>) : Int{
        var result = index
        var nextMin = cups[index]-1

        var absoluteMin = cups.minOrNull() ?: 0
        while (nextMin != 0 && result == index) {
            for ((i,v) in cups.withIndex()){
                if (v == nextMin){
                    result = i
                }
            }
            if (result == index){
                nextMin--
            }
        }

        if (result == index){
            var maxValue = 0
            for ((i,v) in cups.withIndex()){
                if (v > maxValue){
                    maxValue = v
                    result = i
                }
            }
        }

        return result
    }

    fun part1(){
        val input = readFile(filename)
        var cups = input[0].toCharArray().map { i -> i.toString().toInt() }.toMutableList()

        for (round in 1..100){

            var nextThree = cups.subList(1, 4).toList()

            cups.removeAll(nextThree)

            var destIndex = findDestinationIndex(0, cups)
            cups.addAll(destIndex+1, nextThree)

            cups.add(cups[0])
            cups.removeAt(0)
        }
        var result = cups.joinToString("")
        var indexOfOne = result.indexOf("1")
        result = result.substring(indexOfOne) + result.substring(0, indexOfOne)
        result = result.substring(1)
        println("Result is $result")

    }
}

fun main (args : Array<String>){
    Day23("src/Day23Input").run { part1() }
}