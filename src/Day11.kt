import kotlin.math.max
import kotlin.math.min

enum class TileStatus(val char: Char){
    Floor('.'),
    EmptySeat('L'),
    OccupiedSeat('#');
    companion object {
        fun valueOf(value : Char): TileStatus = TileStatus.values().first {it.char == value}
    }
}

class Day11 (filename : String) {
    var floorMap : Array<Array<TileStatus>> = arrayOf();

    fun countOccupiedSeats(map : Array<Array<TileStatus>>, xPos : Int, yPos : Int) : Int{
        var result = 0;
        for (x in max(0,xPos-1) until min(map.size, xPos+2)){
            for (y in max(0, yPos-1) until min(map[0].size, yPos+2)) {
                if (x == xPos && y == yPos) continue;
                if (map[x][y] == TileStatus.OccupiedSeat) result++;
            }
        }
        return result;
    }

    fun deepCopyMap(map : Array<Array<TileStatus>>) : Array<Array<TileStatus>>{
        val result = Array(map.size) { col ->
            Array(map[0].size) { row ->
                TileStatus.Floor
            }
        }

        for (x in 0 until map.size){
            for (y in 0 until map[x].size){
                result[x][y] = map[x][y]
            }
        }

        return result;
    }

    init {
        val stringInput = readFile(filename);

        floorMap = Array(stringInput[0].length) { col ->
            Array(stringInput.size) { row ->
                TileStatus.Floor
            }
        }



        for (y in 0 until stringInput.size){
            for (x in 0 until stringInput[y].length){
                floorMap[x][y] = TileStatus.valueOf(stringInput[y][x])
            }
        }


        var changing = true;
        while (changing){
            changing = false;
            var tempFloorMap = deepCopyMap(floorMap);

            for (x in 0 until floorMap.size){
                for (y in 0 until floorMap[x].size){
                    val occupiedSeats = countOccupiedSeats(floorMap, x, y)
                    when{
                        floorMap[x][y] == TileStatus.EmptySeat && occupiedSeats == 0 -> {
                            tempFloorMap[x][y] = TileStatus.OccupiedSeat
                            changing = true;
                        }
                        floorMap[x][y] == TileStatus.OccupiedSeat && occupiedSeats >= 4 -> {
                            tempFloorMap[x][y] = TileStatus.EmptySeat
                            changing = true;
                        }
                    }
                }
            }
            floorMap = tempFloorMap
        }

        var occupiedSeats = 0
        for (x in 0 until floorMap.size){
            for (y in 0 until floorMap[x].size){
                if (floorMap[x][y] == TileStatus.OccupiedSeat) occupiedSeats++;
            }
        }

        println("Occupied seats in the end: $occupiedSeats")
    }
}
fun main(args : Array<String>){
    Day11("src/Day11Input")
}