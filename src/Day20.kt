import kotlin.math.sqrt


class TileOrientation(map : Array<Array<Char>>, parentTile : Tile, flipped : Boolean, orientation : Int){
    companion object {
        val topEdgesMap = mutableMapOf<String, MutableList<TileOrientation>>()
        val rightEdgesMap = mutableMapOf<String, MutableList<TileOrientation>>()
        val bottomEdgesMap = mutableMapOf<String, MutableList<TileOrientation>>()
        val leftEdgesMap = mutableMapOf<String, MutableList<TileOrientation>>()
    }

    val parentTile : Tile = parentTile
    var edges : List<String> = mutableListOf()
    val flipped = flipped
    val orientation = orientation

    init {

        var topEdge = ""
        var bottomEdge = ""
        var leftEdge = ""
        var rightEdge = ""

        for (x in (0..9)){
            topEdge += map[x][0]
            bottomEdge += map[x][9]
        }
        for (y in (0..9)){
            rightEdge += map[9][y]
            leftEdge += map[0][y]
        }

        topEdgesMap.putIfAbsent(topEdge, mutableListOf())
        topEdgesMap[topEdge]!!.add(this)
        rightEdgesMap.putIfAbsent(rightEdge, mutableListOf())
        rightEdgesMap[rightEdge]!!.add(this)
        bottomEdgesMap.putIfAbsent(bottomEdge, mutableListOf())
        bottomEdgesMap[bottomEdge]!!.add(this)
        leftEdgesMap.putIfAbsent(leftEdge, mutableListOf())
        leftEdgesMap[leftEdge]!!.add(this)

        edges = listOf(topEdge, rightEdge, bottomEdge, leftEdge)
    }
}

enum class TileOrientationStatus {
    tileAlreadyUsed,
    doesNotMatch,
    ok
}

class ImageMesh(dimensions : Int, allTiles : List<Tile>){
    var data : Array<Array<TileOrientation?>>? = null
    var usedTiles = mutableSetOf<Tile>()
    val dimensions = dimensions
    val allTiles = allTiles

    init {
        data = Array(dimensions) {Array(dimensions) {null} }
    }

    fun addTile(x : Int, y : Int, t : TileOrientation) : TileOrientationStatus {
        if (usedTiles.contains(t.parentTile)) return TileOrientationStatus.tileAlreadyUsed
        data!![x][y] = t
        if (!verifyOrientation(x,y)) {
            data!![x][y] = null
            return TileOrientationStatus.doesNotMatch
        }
        usedTiles.add(t.parentTile)
        return TileOrientationStatus.ok
    }

    fun verifyOrientation(x : Int, y : Int) : Boolean {
        if (data!![x][y] == null) return true
        val orientation = data!![x][y]
        var match = true;
        if (x > 0 && data!![x-1][y] != null) {
            val orientation2 = data!![x-1][y]!!
            match = match && orientation2.edges[1] == orientation!!.edges[3]
        }
        if (x < dimensions-1 && data!![x+1][y] != null) {
            val pixel2 = data!![x+1][y]!!
            match = match && pixel2.edges[3] == orientation!!.edges[1]
        }
        if (y > 0 && data!![x][y-1] != null) {
            val pixel2 = data!![x][y-1]!!
            match = match && pixel2.edges[2] == orientation!!.edges[0]
        }
        if (y < dimensions-1 && data!![x][y+1] != null) {
            val pixel2 = data!![x][y+1]!!
            match = match && pixel2.edges[0] == orientation!!.edges[2]
        }
        return match
    }

    fun clone() : ImageMesh {
        val clone = ImageMesh(dimensions, allTiles)
        clone.usedTiles = usedTiles.toMutableSet()
        for (y in 0 until data!!.size){
            for (x in 0 until data!![y].size){
                clone.data!![x][y] = data!![x][y]
            }
        }
        return clone
    }

    fun fillImage() : ImageMesh ?{
        for (y in 0..dimensions-1){
            for (x in 0..dimensions-1) {

                var c_x = x
                var c_y = y

                var t_x = x+1
                var t_y = y

                var lineSwitch = false;
                if (t_x == dimensions){
                    lineSwitch = true;

                    c_x = 0
                    c_y = y
                    t_x = 0
                    t_y = y+1
                    if (t_y == dimensions) break
                }

                val pixel = data!![c_x][c_y] ?: continue
                if (data!![t_x][t_y] != null) continue

                var candidates : List<TileOrientation>? = TileOrientation
                    .leftEdgesMap.getOrElse(pixel!!.edges[1], {
                        listOf<TileOrientation>()})

                if (lineSwitch){
                    val edge = pixel!!.edges[2]
                    candidates = TileOrientation
                        .topEdgesMap.getOrElse(edge, { listOf<TileOrientation>()})
                }

                for (c in candidates!!){
                    if (c.parentTile == pixel.parentTile) continue

                    val clone = clone()
                    if (clone.addTile(t_x,t_y, c) == TileOrientationStatus.ok) {
                        val res = clone.fillImage()
                        if (res != null) {
                            return res
                        }
                    }
                }
                return null
            }
        }
        return this
    }



    fun calculateImageResult() : Long {
        var coordinates = listOf(Pair(0,0), Pair(dimensions-1, dimensions-1),
            Pair(0, dimensions-1), Pair(dimensions-1, 0))
        var result = 1L;

        for (c in coordinates) {
            if (data!![c.first][c.second] != null) {
                result *= data!![c.first][c.second]!!.parentTile.id
            }
        }

        return result;
    }

    fun showResultPart1() {
        for (y in 0 until data!!.size) {
            for (x in 0 until data!!.size) {
                val t = data!![x][y]
                val tileData = t!!.parentTile.orientData(t!!)
                printArray(tileData)
            }
        }
    }

    fun printIDs() {
        for (y in 0 until data!!.size) {
            for (x in 0 until data!!.size) {
                print("${data!![x][y]!!.parentTile.id}\t")
            }
            println()
        }
    }

    fun createWholeImage() : Array<Array<Char>>{
        val length = data!!.size*11
        val result = Array(length){Array(length){' '} }
        for (y in 0 until data!!.size){
            for (x in 0 until data!!.size) {
                val t = data!![x][y]
                val tileData = t!!.parentTile.orientData(t!!)
                for (t_y in 0 until 10){
                    for (t_x in 0 until 10){
                        result[x*11+t_x][y*11+t_y] = tileData[t_x][t_y]
                    }
                }
            }
        }
        return result
    }

    fun create8by8Images() : Array<Array<Char>>{
        val length = data!!.size*8
        val result = Array(length){Array(length){'.'} }
        for (y in 0 until data!!.size){
            for (x in 0 until data!!.size) {
                val t = data!![x][y]
                val tileData = t!!.parentTile.orientData(t!!)
                for (t_y in 0 until 8){
                    for (t_x in 0 until 8){
                        result[x*8+t_x][y*8+t_y] = tileData[t_x+1][t_y+1]
                    }
                }
            }
        }
        return result
    }

    fun searchSeaMonster() : Int{
        var input = create8by8Images()
        printArray(input)
        var result = 0;
        var monsters = 0;
        for (x in 0 until input.size){
            for (y in 0 until input.size){
                if (input[x][y] == '#') {
                    result++
                }
            }
        }

        var rotations = 0
        while (monsters == 0){
            for (x in 0 until input.size-20){
                for (y in 0 until input.size-3){
                    val foundMonster = isMonster(x,y,input)
                    if (foundMonster) {
                        monsters++
                    }
                }
            }
            input = rotate(1, input)
            rotations++
            if (rotations == 4){
                input = flip(input)
            }
        }

        return result - monsters*15
    }

    /**
     * Seamonster takes 15 sharps...
     */
    fun isMonster(x : Int, y : Int, data : Array<Array<Char>>) : Boolean{
        return data[x][y+1] == '#' &&
                data[x+1][y+2] == '#' &&
                data[x+4][y+2] == '#' &&
                data[x+5][y+1] == '#' &&
                data[x+6][y+1] == '#' &&
                data[x+7][y+2] == '#' &&
                data[x+10][y+2] == '#' &&
                data[x+11][y+1] == '#' &&
                data[x+12][y+1] == '#' &&
                data[x+13][y+2] == '#' &&
                data[x+16][y+2] == '#' &&
                data[x+17][y+1] == '#' &&
                data[x+18][y] == '#' &&
                data[x+18][y+1] == '#' &&
                data[x+19][y+1] == '#'
    }

}

class Tile(ID : Int) {
    val id = ID
    val rawStringData = mutableListOf<String>()
    val orientations = mutableListOf<TileOrientation>()
    var map = Array(10){Array(10){'.'} }


    fun addString(input : String){
        rawStringData.add(input)
    }
    fun initializeEdgesAndOrientation(){
        for (y in (0..9)){
            for (x in (0..9)){
                map[x][y] = rawStringData[y][x]
            }
        }
        createOrientations()
    }

    fun orientData(orientation : TileOrientation) : Array<Array<Char>>{
        var tempMap = copyArray(map, 10)

        if (orientation.flipped) {
            tempMap = flip(tempMap)
        }
        tempMap = rotate(orientation.orientation, tempMap)
        return tempMap
    }

    private fun createOrientations(){
        var tempMap = copyArray(map, 10)
        for  (i in 0..3){
            val or = TileOrientation(tempMap, this, false, i)
            orientations.add(or)
            tempMap = rotate(1, tempMap)
        }

        tempMap = flip(tempMap)

        for  (i in 0..3) {
            val or = TileOrientation(tempMap, this, true, i)
            orientations.add(or)
            tempMap = rotate(1, tempMap)
        }
    }
}

class Day20 (filename : String) {
    val filename = filename
    val tiles =  mutableListOf<Tile>()
    fun part1(){
        val input = readFile(filename)
        var currentTile : Tile? = null
        for (i in input){
            if (i == "") continue
            if (i.startsWith("Tile ")) {
                currentTile = Tile(i.substring(5, 9).toInt())
                tiles.add(currentTile)
            }
            else {
                currentTile?.addString(i)
            }
        }

        for (t in tiles) {
            t.initializeEdgesAndOrientation()
        }
        val dimensions = sqrt(tiles.size.toFloat()).toInt()

        var foundSomething = false;
        var finalImage : ImageMesh? = null
        var numberImages = 0
 outer@ for (t in tiles){
            for (o in t.orientations){
                val image = ImageMesh(dimensions,tiles)
                image.addTile(0,0, o)
                finalImage = image.fillImage()
                if (finalImage != null) {
                    numberImages++
                    foundSomething = true;
                    println()
                    println("###")
                    println("There is a result ${finalImage.calculateImageResult()}")
                    //break@outer;
                    printArray(finalImage.createWholeImage())
                    finalImage.printIDs()
                    break@outer
                }
            }
        }
        println("Found a total of $numberImages images!")

        if (finalImage != null){
            val res = finalImage.create8by8Images()
            printArray(res)

            val roughWaters = finalImage!!.searchSeaMonster()
            println("Rough Waters $roughWaters")
        }


    }
}
fun main(args : Array<String>){
    Day20("src/Day20Input").run { part1() }
}