import readFile

open class TemplateDay (day : Int, year : Int){

    open var input : List<String>? = null
    init {
        var dataFile = "data/$year/Day${day}Input"
        input = readFile(dataFile)
    }
    open fun part1(){
        println("Implement part1")
    }
    open fun part2(){
        println("Implement part2")
    }
}

