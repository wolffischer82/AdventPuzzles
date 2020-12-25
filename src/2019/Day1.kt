package `2019`

import TemplateDay

class Day1 : TemplateDay(1, 2019) {
    override fun part1() {
        var result = 0
        for (l in input!!){
            result += (l.toInt() / 3).toInt() - 2
        }
        println ("Result $result")
    }

    fun calculateTotalFuel (mass : Int) : Int{
        var result = (mass / 3).toInt() - 2
        if (result <= 0) return 0
        return result + calculateTotalFuel(result)
    }
    override fun part2() {
        var result = 0
        for (l in input!!){
            result += calculateTotalFuel(l.toInt())
        }
        println ("Result part 2 $result")
    }
}

fun main (args : Array<String>){
    Day1().run { part1(); part2() }
}