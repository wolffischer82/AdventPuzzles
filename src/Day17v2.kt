import kotlin.math.max
import kotlin.math.min

class VariableSpace(dimension : Int) {
    val spaceDimensions = dimension
    var dimensionRanges = mutableListOf<IntRange>()
    val activeCubes = mutableMapOf<Int, Boolean>()

    init {
        for (i in 0..dimension){
            dimensionRanges.add(0..0)
        }
    }

    fun isActive(coordinate : List<Int>) : Boolean{
        val index = calculateCoordinate(coordinate)
        activeCubes.putIfAbsent(index, false)
        return activeCubes[index]!!
    }

    //How to cope with negative coordinates?
    fun checkDimensionRanges(coordinate: List<Int>)
    {
        for ((ind, v) in coordinate.withIndex()){

            dimensionRanges[ind] = min(dimensionRanges[ind].first,v-1)..max(dimensionRanges[ind].last, v+1)
        }
    }

    fun clone() : VariableSpace {
        val result = VariableSpace(spaceDimensions)
        for ((k,v) in activeCubes){
            result.activeCubes[k] = v
        }

        for (i in dimensionRanges){
            result.dimensionRanges.add(i.first..i.last)
        }
        return result
    }

    //How to cope with negative coordinates?
    fun calculateCoordinate(coordinates: List<Int>) : Int {
        val dimensionSizes = dimensionRanges.map { it -> it.last-it.first }
        var coordinate = 0;
        for ((ind, v) in coordinates.withIndex()){

            var multiplier = 1;
            for (m in ind..ind+(dimensionSizes.size-ind-2)){
                multiplier *= dimensionSizes[m]
            }
            coordinate += v*multiplier
        }
        return coordinate
    }

    private fun recursiveCountActiveCubes(originalCoordinate : List<Int>,
                                          coordinate : List<Int>,
                                          searchSpace: List<List<Int>>,
                                          searchIndex : Int) : Int {
        var result = 0;
        if (searchIndex == searchSpace.size-1){

            if (originalCoordinate == coordinate) result = 0
            else if (isActive(coordinate)) result = 1
        } else {
            var index = searchIndex+1
            for (v in searchSpace[index]){
                val tCoordinate = coordinate.toMutableList().apply { add(v) }
                result += recursiveCountActiveCubes(originalCoordinate,
                    tCoordinate, searchSpace, index)
            }
        }
        return result;
    }

    fun countActiveCubes(coordinate: List<Int>) : Int{
        val coordinates = mutableListOf<List<Int>>()
        for (i in coordinate){
            val list = listOf<Int>(i-1,i,i+1)
            coordinates.add(list)
        }

        return recursiveCountActiveCubes(coordinate, mutableListOf(),
            coordinates, -1)
    }

    fun countAllActiveCubes() : Int {
        return activeCubes.count { (k,v) -> v }
    }

    fun setCubeState(coordinates: List<Int>, state : Boolean) {
        val index = calculateCoordinate(coordinates)
        checkDimensionRanges(coordinates)
        activeCubes.putIfAbsent(index, false)
        activeCubes[index] = state
    }
}

class Day17v2 (filename : String){
    val filename = filename
    fun solution(dim : Int){
        val input = readFile(filename)

        var variableSpace = VariableSpace(3)
        for ((y, v_y) in input.withIndex()){
            for ((x, v_x) in v_y.withIndex()){
                var state = false;
                if (v_x == '#') {
                    state = true;
                }
                variableSpace.setCubeState(listOf(x,y,0), state)
            }
        }

        for (i in 1..6){
            val mySpaceMod = variableSpace.clone()

            val searchSpace = mutableListOf<List<Int>>()
            for (i in variableSpace.dimensionRanges){
                searchSpace.add(i.toList())
            }

            checkCoordinates(variableSpace, mutableListOf(), searchSpace, -1)

            variableSpace = mySpaceMod
        }

        println("Part 1 Active cubes in total is ${variableSpace.countAllActiveCubes()}")
    }

    fun checkCoordinates(space : VariableSpace, coordinate : List<Int>, searchSpace: List<List<Int>>, searchIndex : Int){
        if (searchIndex == searchSpace.size-1){
            val activeCubes = space.countActiveCubes(coordinate)
            val isActive = space.isActive(coordinate)

            if (!(isActive && (activeCubes == 2 || activeCubes == 3))){
                space.setCubeState(coordinate,false)
            }

            if (!isActive && activeCubes == 3){
                space.setCubeState(coordinate, true)
            }
        }
        else {
            var index = searchIndex+1
            for (v in searchSpace[index]){
                val tCoordinate = coordinate.toMutableList().apply { add(v) }
                checkCoordinates(space, tCoordinate, searchSpace, index)
            }
        }
    }
}
fun main(args : Array<String>){
    Day17v2("src/Day17Input").run { solution(3); }
}