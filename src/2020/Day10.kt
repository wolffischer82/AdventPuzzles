package `2020`

import readFile

class JoltCombinations(inputList : List<Int>)
{
    var input = listOf<Int>()
    init {
        input = inputList
    }

    fun countLeafs() : Long {
        var result : Long = 1;

        val possibilityList = mutableListOf<Int>();

        for (i in 0 until input.size-1)
        {
            var countPoss = 0;
            for (i2 in i+1 until i+4) {
                if (i2 < input.size && input[i2]-input[i] <=3 )
                {
                    countPoss++;
                }
            }
            possibilityList.add(countPoss);
        }

        println("Difflist: $possibilityList");
        var sum : Long = 0;

        for (i in 0 until possibilityList.size-1)
        {
            if (possibilityList[i] == 1){
                if (sum > 1)
                {
                    if (sum > 3) sum--;
                    result *= sum;
                }
                sum = 0;
            }
            else{

                sum += possibilityList[i];
            }

        }

        return result;
    }
}

class Day10 (filename : String){
    init {
        val input = readFile(filename)
        var joltList = mutableListOf<Int>()
        input.forEach {joltList.add(it.toInt())}
        joltList = joltList.sorted().toMutableList();
        joltList.add(0,0);
        joltList.add(joltList.size, joltList.maxOrNull()!!+3);
        println("Joltlist is $joltList")
        var diff1 = 0
        var diff3 = 0

        for (i in 0 until joltList.size-1)
        {
            val first = joltList[i]
            val second = joltList[i+1]
            if (second-first == 1) diff1++
            if (second-first == 3) diff3++
        }
        println("Difference of 1: $diff1")
        println("Difference of 3: $diff3")
        println("Total result is ${diff1*diff3}")



        var possibilities = JoltCombinations(joltList).countLeafs();

        println("Possibilities is $possibilities");
    }
}
fun main(args : Array<String>){
    Day10("data/2020/Day10Input")
}