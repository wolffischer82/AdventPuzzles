package `2020`

import readFile
import kotlin.math.max
import kotlin.math.min

class Space4D() {
    var activeSpace = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Boolean>>>>()
    var dimension_x = Pair(0,0)
    var dimension_y = Pair(0,0)
    var dimension_z = Pair(0,0)
    var dimension_w = Pair(0,0)

    fun isActive(x : Int, y : Int, z : Int, w:Int) : Boolean {
        checkMapCoordinates(x,y,z,w)
        return activeSpace[x]!![y]!![z]!![w]!!
    }

    fun getDimensionXRange():IntRange{
        return dimension_x.first-1..dimension_x.second+1
    }
    fun getDimensionYRange():IntRange{
        return dimension_y.first-1..dimension_y.second+1
    }
    fun getDimensionZRange():IntRange{
        return dimension_z.first-1..dimension_z.second+1
    }
    fun getDimensionWRange():IntRange{
        return dimension_w.first-1..dimension_w.second+1
    }

    fun checkMapCoordinates(x : Int, y : Int, z : Int, w : Int){
        activeSpace.putIfAbsent(x, mutableMapOf())
        activeSpace[x]!!.putIfAbsent(y, mutableMapOf())
        activeSpace[x]!![y]!!.putIfAbsent(z, mutableMapOf())
        activeSpace[x]!![y]!![z]!!.putIfAbsent(w, false)

    }

    fun setCubeState(x : Int, y : Int, z : Int, w : Int, active : Boolean) {
        checkMapCoordinates(x,y,z,w)
        dimension_x = Pair(min(dimension_x.first, x), max(dimension_x.second, x))
        dimension_y = Pair(min(dimension_y.first, y), max(dimension_y.second, y))
        dimension_z = Pair(min(dimension_z.first, z), max(dimension_z.second, z))
        dimension_w = Pair(min(dimension_w.first, z), max(dimension_w.second, w))

        activeSpace[x]?.get(y)?.get(z)?.set(w, active);
    }

    fun clone() : Space4D {
        val spaceClone = Space4D()
        for (i_x in activeSpace.keys) {
            for (i_y in activeSpace[i_x]!!.keys){
                for (i_z in activeSpace[i_x]!![i_y]!!.keys) {
                    for (i_w in activeSpace[i_x]!![i_y]!![i_z]!!.keys){
                        val state = activeSpace[i_x]!![i_y]!![i_z]!![i_w]!!
                        spaceClone.setCubeState(i_x, i_y, i_z, i_w, state)
                    }
                }
            }
        }
        return spaceClone
    }

    fun retrieveActiveCubes(x : Int, y: Int, z: Int, w: Int) : Int {
        var result = 0;
        for (i_x in x-1..x+1){
            for (i_y in y-1 .. y+1){
                for (i_z in z-1..z+1) {
                    for (i_w in w-1..w+1) {
                        if (i_x == x && i_y == y && i_z == z && i_w == w) continue;
                        if (isActive(i_x,i_y,i_z, i_w)) {
                            result++
                        }
                    }

                }
            }
        }
        return result;
    }

    fun retrieveActiveCubesTotal() : Int {
        var result = 0;
        for (i_x in activeSpace.keys) {
            for (i_y in activeSpace[i_x]!!.keys){
                for (i_z in activeSpace[i_x]!![i_y]!!.keys) {
                    for (i_w in activeSpace[i_x]!![i_y]!![i_z]!!.keys) {
                        if (activeSpace[i_x]!![i_y]!![i_z]!![i_w]!!){
                            result++
                        }
                    }
                }
            }
        }
        return result;
    }
}

class Space() {
    var activeSpace = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, Boolean>>>()
    var dimension_x = Pair(0,0);
    var dimension_y = Pair(0,0);
    var dimension_z = Pair(0,0)

    fun isActive(x : Int, y : Int, z : Int) : Boolean {
        checkMapCoordinates(x,y,z)
        return activeSpace[x]!![y]!![z]!!
    }

    fun getDimensionXRange():IntRange{
        return dimension_x.first-1..dimension_x.second+1
    }
    fun getDimensionYRange():IntRange{
        return dimension_y.first-1..dimension_y.second+1
    }
    fun getDimensionZRange():IntRange{
        return dimension_z.first-1..dimension_z.second+1
    }

    fun checkMapCoordinates(x : Int, y : Int, z : Int){
        activeSpace.putIfAbsent(x, mutableMapOf())
        activeSpace[x]!!.putIfAbsent(y, mutableMapOf())
        activeSpace[x]!![y]!!.putIfAbsent(z, false)
    }

    fun setCubeState(x : Int, y: Int, z: Int, active : Boolean) {
        checkMapCoordinates(x,y,z)
        dimension_x = Pair(min(dimension_x.first, x), max(dimension_x.second, x))
        dimension_y = Pair(min(dimension_y.first, y), max(dimension_y.second, y))
        dimension_z = Pair(min(dimension_z.first, z), max(dimension_z.second, z))

        activeSpace[x]?.get(y)?.set(z, active);
    }

    fun clone() : Space {
        val spaceClone = Space()
        for (i_x in activeSpace.keys) {
            for (i_y in activeSpace[i_x]!!.keys){
                for (i_z in activeSpace[i_x]!![i_y]!!.keys) {
                    val state = activeSpace[i_x]!![i_y]!![i_z]!!
                    spaceClone.setCubeState(i_x, i_y, i_z, state)
                }
            }
        }
        return spaceClone
    }

    fun retrieveActiveCubes(x : Int, y: Int, z: Int) : Int {
        var result = 0;
        for (i_x in x-1..x+1){
            for (i_y in y-1 .. y+1){
                for (i_z in z-1..z+1) {
                    if (i_x == x && i_y == y && i_z == z) continue;
                    if (isActive(i_x,i_y,i_z)) {
                        result++
                    }
                }
            }
        }
        return result;
    }

    fun retrieveActiveCubesTotal() : Int {
        var result = 0;
        for (i_x in activeSpace.keys) {
            for (i_y in activeSpace[i_x]!!.keys){
                for (i_z in activeSpace[i_x]!![i_y]!!.keys) {
                    if (activeSpace[i_x]!![i_y]!![i_z]!!){
                        result++
                    }
                }
            }
        }
        return result;
    }
}

class Day17 (filename : String){
    val filename = filename



    fun part1(){
        val input = readFile(filename)
        var mySpace = Space()
        for ((i_y,v_y) in input.withIndex()){
            for ((i_x,v_x) in v_y.withIndex()){
                if (v_x == '#') {
                    mySpace.setCubeState(i_x, i_y, 0, true)
                }
            }
        }

        for (i in 1..6){
            val mySpaceMod = mySpace.clone()

            for (x in mySpace.getDimensionXRange()) {
                for (y in mySpace.getDimensionYRange()) {
                    for (z in mySpace.getDimensionZRange()) {

                        val activeCubes = mySpace.retrieveActiveCubes(x,y,z)
                        val isActive = mySpace.isActive(x,y,z)

                        if (!(isActive && (activeCubes == 2 || activeCubes == 3))){
                            mySpaceMod.setCubeState(x,y,z,false)
                        }

                        if (!isActive && activeCubes == 3){
                            mySpaceMod.setCubeState(x,y,z, true)
                        }
                    }
                }
            }
            mySpace = mySpaceMod
        }

        println("Part 1 Active cubes in total is ${mySpace.retrieveActiveCubesTotal()}")

    }

    fun part2() {
        val input = readFile(filename)
        var mySpace = Space4D()
        for ((i_y,v_y) in input.withIndex()){
            for ((i_x,v_x) in v_y.withIndex()){
                if (v_x == '#') {
                    mySpace.setCubeState(i_x, i_y, 0, 0,true)
                }
            }
        }

        for (i in 1..6){
            val mySpaceMod = mySpace.clone()

            for (x in mySpace.getDimensionXRange()) {
                for (y in mySpace.getDimensionYRange()) {
                    for (z in mySpace.getDimensionZRange()) {
                        for (w in mySpace.getDimensionWRange()) {
                            val activeCubes = mySpace.retrieveActiveCubes(x,y,z,w)
                            val isActive = mySpace.isActive(x,y,z,w)

                            if (!(isActive && (activeCubes == 2 || activeCubes == 3))){
                                mySpaceMod.setCubeState(x,y,z, w,false)
                            }

                            if (!isActive && activeCubes == 3){
                                mySpaceMod.setCubeState(x,y,z, w, true)
                            }
                        }
                    }
                }
            }
            mySpace = mySpaceMod
        }

        println("Part 2 Active cubes in total is ${mySpace.retrieveActiveCubesTotal()}")
    }
}
fun main(args : Array<String>){
    Day17("data/2020/Day17Input").run { part1(); part2() }
}