package `2020`

import readFile

class ListNode (v : Int){
    val value = v
    var nextNode : ListNode? = null

    fun print(){
        println()
        print("Starting at $value ")
        nextNode!!.print(this)
    }
    fun print(startingNode : ListNode){
        if (this == startingNode) return
        print("$value ")
        nextNode?.print(startingNode)
    }
}
class LinkedCircleList {
    var head : ListNode? = null
    var map = mutableMapOf<Int, ListNode>()

    fun size() : Int {
        return map.size
    }

    fun insert(n : Int){
        val newNode = ListNode(n)
        if (head == null){
            head = newNode
            newNode.nextNode = newNode
        }
        else {
            val nextNode = head?.nextNode
            head?.nextNode = newNode
            newNode.nextNode = nextNode
            head = head?.nextNode
        }
        map[n] = newNode
    }

    fun repositionHead(n : Int) : ListNode? {
        head = map[n]
        return head
    }

    fun findNode(n : Int) : ListNode? {
        if (map.containsKey(n)){
            return map[n]
        }
        return null
    }

    fun removeNext() : ListNode?{
        if (head == null) return null;
        val result = head?.nextNode

        if (result != null){
            map.remove(result.value)
            head?.nextNode = result?.nextNode
        }

        return result
    }

    fun current() : ListNode? {
        return head
    }
    fun getNext() : ListNode? {
        head = head?.nextNode
        return head
    }
}

class Day23 (filename : String){
    val filename = filename

    fun findDestinationIndex(index : Int, cups : List<Int>) : Int{
        var result = index
        var nextMin = cups[index]-1

        var absoluteMin = cups.minOrNull() ?: 0

        while (nextMin != 0 && result == index) {
            for ((i,v) in cups.withIndex()){
                if (v == nextMin){
                    result = i
                }
            }
            if (result == index){
                nextMin--
            }
        }

        if (result == index){
            var maxValue = 0
            for ((i,v) in cups.withIndex()){
                if (v > maxValue){
                    maxValue = v
                    result = i
                }
            }
        }

        return result
    }

    fun findDestinationIndex2(value : Int, cups : LinkedCircleList, excluded : Set<ListNode?>) : ListNode {
        var nextMin = value
        var result = cups.findNode(value)

        while (nextMin > 0){
            result = cups.findNode(nextMin)

            nextMin--
            if (result == null) continue
            if (result != cups.findNode(value)) break
        }

        if (result == null || result == cups.findNode(value)){
            var max = cups.size() + excluded.size
            var found = true
            while (found){
                found = false
                for (n in excluded){
                    if (n!!.value == max){
                        found = true
                        max--
                    }
                }
            }
            result = cups.findNode(max)
        }

        return result!!
    }

    fun part1(){
        val input = readFile(filename)
        var cups = input[0].toCharArray().map { i -> i.toString().toInt() }.toMutableList()

        for (round in 1..100){

            var nextThree = cups.subList(1, 4).toList()

            cups.removeAll(nextThree)

            var destIndex = findDestinationIndex(0, cups)

            cups.addAll(destIndex+1, nextThree)

            cups.add(cups[0])
            cups.removeAt(0)
        }
        var result = cups.joinToString("")
        var indexOfOne = result.indexOf("1")
        result = result.substring(indexOfOne) + result.substring(0, indexOfOne)
        result = result.substring(1)
        println("Result is $result")

    }

    fun part2(){
        val input = readFile(filename)
        var cups_temp = input[0].toCharArray().map { i -> i.toString().toInt() }.toList()
        var cups = LinkedCircleList()
        cups_temp.forEach { t -> cups.insert(t) }

        for (i in 10..1000000){
            cups.insert(i)
        }
        cups.repositionHead(cups_temp[0])
        for (round in 1..10000000){
            var current = cups.current()
            var firstElem = cups.removeNext()
            var secondElem = cups.removeNext()
            var thirdElement = cups.removeNext()


            var insertAfter = findDestinationIndex2(current!!.value
                , cups
                , setOf(firstElem, secondElem, thirdElement))

            cups.repositionHead(insertAfter.value)

            cups.insert(firstElem!!.value)
            cups.insert(secondElem!!.value)
            cups.insert(thirdElement!!.value)

            cups.repositionHead(current.value)
//            current.print()
            cups.getNext()
        }


        cups.repositionHead(1)
        val firstElem = cups.getNext()!!.value.toLong()
        val secondElem = cups.getNext()!!.value.toLong()
        val result : Long = firstElem * secondElem
        println("Result is $result")

    }

}

fun main (args : Array<String>){
    Day23("data/2020/Day23Input").run { part1(); part2() }
}