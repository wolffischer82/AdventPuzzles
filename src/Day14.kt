import kotlin.math.pow

class Day14(filename : String) {
    val filename = filename
    fun part1() {
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

    class MemStrNode(memoryAddress : String) {
        var children = mutableListOf<MemStrNode>()
        var address = ""
        init {
            address= memoryAddress
        }

        fun addNewAddress(address : String) {
            if (children.size == 0){
                children.add(MemStrNode("0" + address));
                children.add(MemStrNode("1" + address));
            }
            else {
                for (i in children){
                    i.addNewAddress(address)
                }
            }
        }

        fun returnAddresses() : List<String> {
            if (children.size == 0) return listOf(address)
            val returnList = mutableListOf<String>()
            for (i in children){
                val childrenResults = i.returnAddresses()
                for (childAddress in childrenResults) {
                    returnList.add(address + childAddress)
                }
            }
            return returnList
        }
    }

    fun part2() {
        val input = readFile(filename)
        val memoryMap : MutableMap<Long, Long> = mutableMapOf()
        var currentMask : String = ""
        var overwrittenMemory = 0;
        val zeroBitSet = "0000000000000000000000000000000000000000000000000000000000000000"
        var numX = 0;
        for (line in input){
            val splitters = line.split(" = ")
            when {
                splitters[0].startsWith("mask") -> {
                    currentMask = splitters[1]
                }
                splitters[0].startsWith("mem") -> {
                    val memAddress = splitters[0].split("[")[1].split("]")[0].toLong()
                    var value = splitters[1].toLong()

                    var memoryAddressBinary = java.lang.Long.toBinaryString(memAddress)
                    memoryAddressBinary = zeroBitSet.substring(0, 64-memoryAddressBinary.length) + memoryAddressBinary
                    var currentMaskMod = zeroBitSet.substring(0, 64-currentMask.length) + currentMask

                    for (i in 0 until memoryAddressBinary.length-1){
                        if (memoryAddressBinary[i] == '1' && currentMaskMod[i] != 'X'){
                            currentMaskMod = currentMaskMod.substring(0,i) + '1' + currentMaskMod.substring(i+1);
                        }
                    }

                    var maskSplit = currentMaskMod.split("X")
                    var firstNode : MemStrNode = MemStrNode(maskSplit[0])

                    for (i in 1 until maskSplit.size){
                        firstNode.addNewAddress(maskSplit[i])
                    }

                    val addressList = mutableListOf<String>();
                    if (firstNode != null) {
                        addressList.addAll(firstNode.returnAddresses())
                    }

                    for (i in addressList){
                        val address = java.lang.Long.parseLong(i,2)
                        memoryMap[address] = value
                    }
                }
            }
        }
        println("Num of X is $numX")
        println("Result Part 2 is ${memoryMap.values.sum()}");
        println("Overwritten memory addresses are $overwrittenMemory")

    }
}
fun main (args : Array<String>){
    Day14("src/Day14Input").run { part1(); part2() }
}