package `2019`

import TemplateDay
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day3 : TemplateDay(3,2019) {

    fun printMap(map : Map<Pair<Int,Int>,MutableSet<Int>>){
        var left = 0
        var right = 0
        var top = 0
        var bottom = 0
        for ((k,x) in map){
            left = min(left, k.first)
            right = max(right, k.first)
            top = min(top, k.second)
            bottom = max(bottom, k.second)
        }
        for (y in top..bottom){
            for (x in left..right){
                if (x == 0 && y == 0){
                    print("O")
                }
                else{
                    val p = Pair(x,y)
                    if (!map.containsKey(p)){
                        print(" ")
                    }else{
                        val v = map[p]
                        if (v!!.size == 1){
                            print ("*")
                        } else {
                            print ("X")
                        }
                    }
                }
            }
            println()
        }
    }

    override fun part1() {
        val map = mutableMapOf<Pair<Int,Int>, MutableSet<Int>>()

        var maxIntersections = 0
        var x = 0
        var y = 0
        var wireID = 0
        fun putPosInMap(wireID : Int){
            val p = Pair(x,y)
            map.putIfAbsent(p, mutableSetOf())
            map[p]!!.add(wireID)
            maxIntersections = max(maxIntersections, map[p]!!.size)
        }
        putPosInMap(wireID)

        for (l in input!!){

            val commands = l.split(",")
            x = 0
            y = 0
            for (c in commands){
                val value = c.substring(1).toInt()

                when {
                    c.startsWith("R") -> {
                        for (i in 0 until value){
                            x++
                            putPosInMap(wireID)
                        }
                    }
                    c.startsWith("D") -> {
                        for (i in 0 until value){
                            y++
                            putPosInMap(wireID)
                        }
                    }
                    c.startsWith("L") -> {
                        for (i in 0 until value){
                            x--
                            putPosInMap(wireID)
                        }
                    }
                    c.startsWith("U") -> {
                        for (i in 0 until value){
                            y--
                            putPosInMap(wireID)
                        }
                    }
                }
            }
            wireID++
        }
        val mapMaxInt = map.filter { (k,v) -> v.size == maxIntersections }
        var shortestManhattanDistance = 99999999
        for ((k,v) in mapMaxInt){
            val dist = abs(k.first) + abs(k.second)
            shortestManhattanDistance = min(shortestManhattanDistance, dist)
        }
        println("Shortest Distance is $shortestManhattanDistance")


    }

    override fun part2() {
        super.part2()
    }
}

fun main (args : Array<String>){
    Day3().run { part1(); part2() }
}