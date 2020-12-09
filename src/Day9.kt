class Day9 (filename : String) {
    var numberList = mutableListOf<Long>()
    var preambleSize = 25;

    init {
        val input = readFile(filename)
        numberList = input.map { i -> i.toLong() }.toMutableList()


        for (i in preambleSize..numberList.size-1){
            val tempMap : MutableSet<Long> = mutableSetOf();
            numberList.subList(i-preambleSize, i).forEach { tempMap.add(numberList[i]-it)};
            var isCorrect = false;
            numberList.subList(i-preambleSize, i).forEach { if (tempMap.contains(it)) isCorrect = true }
            if (!isCorrect)
            {
                println("Wrong element is ${numberList[i]}")
                break;
            }
        }
    }
}

fun main (args : Array<String>){
    Day9("src/Day9Input").run {

    }
}