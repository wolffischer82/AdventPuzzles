class Day24 (filename : String){
    val filename = filename
    fun part1(){
        val input = readFile(filename)
    }
}

fun main (args : Array<String>){
    Day24("src/Day24Input").run { part1() }
}