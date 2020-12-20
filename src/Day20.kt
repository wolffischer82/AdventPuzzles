import kotlin.math.sqrt


class TileOrientation(edges : List<String>, parentTile : Tile, flipped : Boolean, orientation : Int){
    companion object {
        val topEdgesMap = mutableMapOf<String, MutableList<TileOrientation>>()
        val rightEdgesMap = mutableMapOf<String, MutableList<TileOrientation>>()
        val bottomEdgesMap = mutableMapOf<String, MutableList<TileOrientation>>()
        val leftEdgesMap = mutableMapOf<String, MutableList<TileOrientation>>()
    }

    val parentTile : Tile = parentTile
    val edges : List<String> = edges
    val flipped = flipped
    val orientation = orientation



    init {
        topEdgesMap.putIfAbsent(edges[0], mutableListOf())
        topEdgesMap[edges[0]]!!.add(this)
        rightEdgesMap.putIfAbsent(edges[1], mutableListOf())
        rightEdgesMap[edges[1]]!!.add(this)
        bottomEdgesMap.putIfAbsent(edges[2], mutableListOf())
        bottomEdgesMap[edges[2]]!!.add(this)
        leftEdgesMap.putIfAbsent(edges[3], mutableListOf())
        leftEdgesMap[edges[3]]!!.add(this)
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
        for (y in 0..data!!.size-1){
            for (x in 0..data!![y].size-1){
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

    private fun createWholeImage() : Array<Array<Char>>{
        val length = data!!.size*8
        val result = Array(length){Array(length){'.'} }
        for (y in 0 until data!!.size){
            for (x in 0 until data!!.size) {
                val t = data!![x][y]
                val tileData = t!!.parentTile.orientData(t!!)
                for (t_y in 0 until 8){
                    for (t_x in 0 until 8){
                        result[x*8+t_x][y*8+t_y] = tileData[x][y]
                    }
                }
            }
        }
        return result
    }

    fun searchSeaMonster() : Int{
        var input = createWholeImage()
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
    val rawData = mutableListOf<String>()

    val originalEdges = mutableListOf<String>()
    val orientations = mutableListOf<TileOrientation>()
    var dataWithoutEdges = Array(8){Array(8){'.'} }


    fun addString(input : String){
        rawData.add(input)
    }
    fun initializeEdgesAndOrientation(){
        var leftEdge = ""
        var rightEdge = ""
        for (l in rawData){
            leftEdge += l[0]
            rightEdge += l[l.length-1]
        }

        for (y in (1..8)){
            for (x in (1..8)){
                dataWithoutEdges[x-1][y-1] = rawData[y][x]
            }
        }

        //0 is up, 1 is right, 2 is down, 3 is left
        originalEdges.add(rawData[0])
        originalEdges.add(rightEdge)
        originalEdges.add(rawData[rawData.size-1])
        originalEdges.add(leftEdge)
        createOrientations()
        flip(dataWithoutEdges)
    }

    fun orientData(orientation : TileOrientation) : Array<Array<Char>>{

        if (orientation.flipped) {
            dataWithoutEdges = flip(dataWithoutEdges)
        }
        dataWithoutEdges = rotate(orientation.orientation, dataWithoutEdges)
        return dataWithoutEdges
    }

    private fun createOrientations(){
        for  (i in 0..3){
            val or = TileOrientation(originalEdges.toList(), this, false, i)
            orientations.add(or)

            val lastEdge = originalEdges[3]
            originalEdges[3] = originalEdges[2]
            originalEdges[2] = originalEdges[1].reversed()
            originalEdges[1] = originalEdges[0]
            originalEdges[0] = lastEdge.reversed()
        }

        var flippedEdges = originalEdges.toMutableList()
        var topEdge = flippedEdges[0]
        flippedEdges[0] = flippedEdges[2]
        flippedEdges[2] = topEdge
        flippedEdges[1] = flippedEdges[1].reversed()
        flippedEdges[3] = flippedEdges[3].reversed()

        for  (i in 0..3) {
            val or = TileOrientation(flippedEdges.toList(), this, true, i)
            orientations.add(or)

            val lastEdge = flippedEdges[3]
            flippedEdges[3] = flippedEdges[2]
            flippedEdges[2] = flippedEdges[1].reversed()
            flippedEdges[1] = flippedEdges[0]
            flippedEdges[0] = lastEdge.reversed()
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
 outer@ for (t in tiles){
            for (o in t.orientations){
                val image = ImageMesh(dimensions,tiles)
                image.addTile(0,0, o)
                finalImage = image.fillImage()
                if (finalImage != null) {
                    foundSomething = true;
                    println("There is a result ${finalImage.calculateImageResult()}")
                    break@outer;
                }
            }
        }
        if (!foundSomething) {
            println("Found nothing!")
        }

        val roughWaters = finalImage!!.searchSeaMonster()
        println("Rough Waters $roughWaters")


    }
}
fun main(args : Array<String>){
    Day20("src/Day20Input").run { part1() }
}