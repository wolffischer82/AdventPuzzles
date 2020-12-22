class Day23 (filename : String){
    val filename = filename
    fun part1(){
        val input = readFile(filename)
    }
}

fun main (args : Array<String>){
    Day23("src/Day23Input").run { part1() }
}