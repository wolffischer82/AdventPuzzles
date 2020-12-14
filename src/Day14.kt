
class Day14(filename : String) {
    init {
        val input = readFile(filename)
        val memoryMap : MutableMap<Int, Long> = mutableMapOf()
        var currentMask : String = ""
        val zeroBitSet = "0000000000000000000000000000000000000000000"
        for (line in input){
            val splitters = line.split(" = ")
            when {
                splitters[0].startsWith("mask") -> {
                    currentMask = splitters[1]
                }
                splitters[0].startsWith("mem") -> {
                    val memAddress = splitters[0].split("[")[1].split("]")[0].toInt()
                    var value = splitters[1].toInt()
                    var bitSetOriginal = Integer.toBinaryString(value)
                    var bitSet = zeroBitSet.substring(0, currentMask.length-bitSetOriginal.length) + bitSetOriginal
                    for (i in 0 until currentMask.length){
                        if (currentMask[i] == 'X') continue
                        else bitSet = bitSet.substring(0, i) + currentMask[i] + bitSet.substring(i+1)
                    }
                    val parsedNumber = java.lang.Long.parseLong(bitSet,2)
                    memoryMap[memAddress] = parsedNumber
                }
            }
        }
        val result = memoryMap.values.sum()
        println("Result Part 1 is $result");
    }
}
fun main (args : Array<String>){
    Day14("src/Day14Input")
}