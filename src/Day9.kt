class Day9 (filename : String) {
    var numberList = mutableListOf<Long>()
    var preambleSize = 25;

    init {
        val input = readFile(filename)
        numberList = input.map { i -> i.toLong() }.toMutableList()
        var wrongNumber : Long = 0;

        for (i in preambleSize until numberList.size){
            val tempMap : MutableSet<Long> = mutableSetOf();
            numberList.subList(i-preambleSize, i).forEach { tempMap.add(numberList[i]-it)};
            var isCorrect = false;
            numberList.subList(i-preambleSize, i).forEach { if (tempMap.contains(it)) isCorrect = true }
            if (!isCorrect)
            {
                wrongNumber = numberList[i];
                println("Wrong element is ${numberList[i]}")
                break;
            }
        }

        var resultPart2 : Long = 0;
loop@   for (i in 0 until numberList.size){
            for (i2 in i+1 until numberList.size){
                val sum = numberList.subList(i, i2).sum()
                if (sum == wrongNumber){
                    numberList.subList(i,i2).run { resultPart2 = minOrNull()!!.plus(this.maxOrNull()!!) };
                    break@loop
                }
                if (sum > wrongNumber){
                    continue@loop
                }

            }
        }
        println("Result part2 $resultPart2")
    }
}

fun main (args : Array<String>){
    Day9("src/Day9Input").run {

    }
}