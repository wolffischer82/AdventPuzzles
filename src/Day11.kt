import kotlin.math.floor
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

    fun countOccupiedSeats2(map : Array<Array<TileStatus>>, xPos : Int, yPos : Int) : Int{
        var result = 0;

        var viewingValidationMap : Array<Array<Boolean>> = Array(3) {col ->
            Array(3) { row ->
                false;
            }
        }

        viewingValidationMap[1][1] = true;

        fun validatePosition(xVal : Int, yVal : Int, xMap : Int, yMap : Int) : Int{
            var result = 0;
            if (!viewingValidationMap[xVal][yVal])
            {
                if (map[xMap][yMap] == TileStatus.OccupiedSeat){
                    viewingValidationMap[xVal][yVal] = true;
                    result++;
                }
                if (map[xMap][yMap] == TileStatus.EmptySeat){
                    viewingValidationMap[xVal][yVal] = true;
                }
            }
            return result;
        }

        var ci = 1;
        while (!viewingValidationMap.all { it.all { i -> i }  }){

            //top left
            if (xPos-ci >= 0 && yPos-ci >= 0){
                result += validatePosition(0,0, xPos-ci, yPos-ci)
            } else {
                viewingValidationMap[0][0] = true;
            }

            //top
            if (yPos-ci >= 0){
                result += validatePosition(1,0, xPos, yPos-ci);
            } else {
                viewingValidationMap[1][0] = true;
            }

            //top right
            if (xPos+ci < map.size && yPos-ci >= 0) {
                result += validatePosition(2,0, xPos+ci, yPos-ci);
            }else{
                viewingValidationMap[2][0] = true;
            }

            //right
            if (xPos+ci < map.size) {
                result += validatePosition(2,1, xPos+ci, yPos);
            }else {
                viewingValidationMap[2][1] = true;
            }

            //bottom right
            if (xPos+ci < map.size && yPos+ci < map[0].size){
                result += validatePosition(2,2, xPos+ci, yPos+ci);
            } else {
                viewingValidationMap[2][2] = true;
            }

            //bottom
            if (yPos+ci < map[0].size){
                result += validatePosition(1,2, xPos, yPos+ci);
            } else {
                viewingValidationMap[1][2] = true;
            }

            //bottom left
            if (xPos-ci >= 0 && yPos+ci < map[0].size){
                result += validatePosition(0,2, xPos-ci, yPos+ci);
            }else {
                viewingValidationMap[0][2] = true;
            }

            //left
            if (xPos-ci >= 0){
                result += validatePosition(0,1, xPos-ci, yPos);
            }else{
                viewingValidationMap[0][1] = true;
            }
            ci++;
        }

        return result;
    }

    fun deepCopyMap(map : Array<Array<TileStatus>>) : Array<Array<TileStatus>>{
        val result = Array(map.size) { col ->
            Array(map[0].size) { row ->
                TileStatus.Floor
            }
        }

        for (x in map.indices){
            for (y in map[x].indices){
                result[x][y] = map[x][y]
            }
        }

        return result;
    }

    val filename : String = filename

    fun part1() {
        val stringInput = readFile(filename);

        floorMap = Array(stringInput[0].length) { col ->
            Array(stringInput.size) { row ->
                TileStatus.Floor
            }
        }



        for (y in stringInput.indices){
            for (x in stringInput[y].indices){
                floorMap[x][y] = TileStatus.valueOf(stringInput[y][x])
            }
        }


        var changing = true;
        while (changing){
            changing = false;
            var tempFloorMap = deepCopyMap(floorMap);

            for (x in floorMap.indices){
                for (y in floorMap[x].indices){
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
        for (x in floorMap.indices){
            for (y in floorMap[x].indices){
                if (floorMap[x][y] == TileStatus.OccupiedSeat) occupiedSeats++;
            }
        }

        println("Occupied seats in the end - Part 1: $occupiedSeats")
    }

    fun part2() {
        val stringInput = readFile(filename);

        floorMap = Array(stringInput[0].length) { col ->
            Array(stringInput.size) { row ->
                TileStatus.Floor
            }
        }



        for (y in stringInput.indices){
            for (x in stringInput[y].indices){
                floorMap[x][y] = TileStatus.valueOf(stringInput[y][x])
            }
        }


        var changing = true;
        while (changing){
            changing = false;
            var tempFloorMap = deepCopyMap(floorMap);

            for (x in floorMap.indices){
                for (y in floorMap[x].indices){
                    val occupiedSeats = countOccupiedSeats2(floorMap, x, y)
                    when{
                        floorMap[x][y] == TileStatus.EmptySeat && occupiedSeats == 0 -> {
                            tempFloorMap[x][y] = TileStatus.OccupiedSeat
                            changing = true;
                        }
                        floorMap[x][y] == TileStatus.OccupiedSeat && occupiedSeats >= 5 -> {
                            tempFloorMap[x][y] = TileStatus.EmptySeat
                            changing = true;
                        }
                    }
                }
            }
            floorMap = tempFloorMap

        }

        var occupiedSeats = 0
        for (x in floorMap.indices){
            for (y in floorMap[x].indices){
                if (floorMap[x][y] == TileStatus.OccupiedSeat) occupiedSeats++;
            }
        }

        println("Occupied seats in the end - Part 2: $occupiedSeats")
    }
}
fun main(args : Array<String>){
    Day11("src/Day11Input").run { part1(); part2()}
}