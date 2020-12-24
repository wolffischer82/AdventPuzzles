enum class HexTile {
    None,
    Black,
    White
}
class HexMap(dimX : Int, dimY : Int){
    val map = Array(dimY) { Array(dimY) {HexTile.White} }
    val dimensionX = dimX
    val dimensionY = dimY

    fun setOrFlipTile(x : Int, y : Int) {
        if (map[x][y] == HexTile.White){
            map[x][y] = HexTile.Black
        }
        else if (map[x][y] == HexTile.Black){
            map[x][y] = HexTile.White
        }
    }

    fun countBlackTiles() : Int {
        var result = 0
        for (x in 0 until dimensionX){
            for (y in 0 until dimensionY){
                if (map[x][y] == HexTile.Black){
                    result++
                }
            }
        }
        return result
    }

    fun countAdjacentBlackTiles(x : Int, y : Int) : Int {
        var result = 0
        val coords = setOf(Pair(x+2,y), Pair(x+1, y+1), Pair(x-1,y+1), Pair(x-2,y), Pair(x-1,y-1), Pair(x+1,y-1))
        for (p in coords){
            if (p.first >= 0 && p.first < dimensionX
                && p.second >= 0 && p.second < dimensionY){
                if (map[p.first][p.second] == HexTile.Black){
                    result++
                }
            }
        }
        return result
    }

    fun clone() : HexMap {
        val result = HexMap(dimensionX, dimensionY)
        for (x in 0 until dimensionX){
            for (y in 0 until dimensionY){
                result.map[x][y] = map[x][y]
            }
        }
        return result
    }

}

class Day24 (filename : String){
    val filename = filename
    val dimX = 500
    val dimY = 500
    val map = HexMap(dimX,dimY)

    fun part1(){
        val input = readFile(filename)
        for (flipCommands in input){
            var c_x = (dimX / 2).toInt()
            var c_y = (dimY / 2).toInt()
            var l = flipCommands
            while (l.length > 0){
                when {
                    l.startsWith("e") -> {
                        c_x += 2
                        l = l.removePrefix("e")
                    }
                    l.startsWith("se") -> {
                        c_x += 1
                        c_y += 1
                        l = l.removePrefix("se")
                    }
                    l.startsWith("sw") -> {
                        c_y += 1
                        c_x -= 1
                        l = l.removePrefix("sw")
                    }
                    l.startsWith("w") -> {
                        c_x -= 2
                        l = l.removePrefix("w")
                    }
                    l.startsWith("nw") -> {
                        c_x -= 1
                        c_y -= 1
                        l = l.removePrefix("nw")
                    }
                    l.startsWith("ne") -> {
                        c_x += 1
                        c_y -= 1
                        l = l.removePrefix("ne")
                    }
                }
            }

            map.setOrFlipTile(c_x, c_y)

        }
        var result = map.countBlackTiles()
        println("There are $result black tiles")

    }

    fun part2(){
        var flipMap = map.clone()
        for (round in 1..100){
            var mapClone = flipMap.clone()
            for (x in 0 until flipMap.dimensionX){
                for (y in 0 until flipMap.dimensionY) {

                    val tileStyle = flipMap.map[x][y]
                    val countBlackTiles = flipMap.countAdjacentBlackTiles(x,y)

                    if (tileStyle == HexTile.Black && (countBlackTiles == 0 || countBlackTiles > 2)){
                        mapClone.setOrFlipTile(x,y)
                    }
                    if (tileStyle == HexTile.White && countBlackTiles == 2){
                        mapClone.setOrFlipTile(x,y)
                    }
                }
            }
            flipMap = mapClone
            val blackTiles = flipMap.countBlackTiles()
            println("Tiles after $round days: $blackTiles")
        }
    }
}

fun main (args : Array<String>){
    Day24("src/Day24Input").run { part1(); part2() }
}