package `2020`

import readFile

open class TemplateDay (year : Int, day : Int){
    var day = day
    var year = year
    var dataFile = "src/2020/Day${day}Input"
    var input : List<String>? = null
    init {
        input = readFile(dataFile)
    }
    open fun part1(){
        println("Implement part1")
    }
    open fun part2(){
        println("Implement part2")
    }
}