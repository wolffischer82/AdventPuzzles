import java.lang.Exception
import kotlin.math.ceil

enum class TileTypes(val char: Char)
{
    Empty('.'),
    Tree('#'),
    EncounterEmpty('O'),
    EncounterTree('X');

    companion object {
        fun valueOf(value : Char): TileTypes = TileTypes.values().first {it.char == value}
    }
}

class Map(val fileName:String)
{
    val map : Array<Array<TileTypes>>
    var mapSizeVertical = 0;
    val maxStepSize = 10;
    var mapSizeHorizontalFactor = 0;
    init {
        val stringMap: List<String> = readFile(fileName);
        mapSizeVertical = stringMap.size;
        val mapSizeHorizontalOnce = stringMap[0].length;

        mapSizeHorizontalFactor = ceil(mapSizeVertical.toDouble()*maxStepSize.toDouble() / mapSizeHorizontalOnce.toDouble()).toInt();
        println("MapSizeHorizontal Factor: $mapSizeHorizontalFactor");
        val mapSizeHorizontal = mapSizeHorizontalOnce * mapSizeHorizontalFactor;
        println("Total Mapsize: $mapSizeHorizontal");
        println("Read map with a total of ${stringMap.size} lines");
        println("Each line has a size of $mapSizeHorizontalOnce entries");
        println("Max Width equals $mapSizeHorizontal");

        map = Array(mapSizeHorizontal) { Array(mapSizeVertical) { TileTypes.Empty } };

        for ((y, mapLine) in stringMap.withIndex()) {
            for ((x, char) in mapLine.toCharArray().withIndex()) {
                for (i in IntRange(0, mapSizeHorizontalFactor - 1)) {
                    val xNew = x + mapSizeHorizontalOnce * i;
                    map[xNew][y] = TileTypes.valueOf(char);
                }
            }
        }
    }
    fun countTrees(xOffset : Int, yOffset : Int) : Int{
        if (xOffset >= mapSizeHorizontalFactor)
        {
            throw Exception("Steps too big for the map...");
        }
        var y = 0;
        var x = 0;
        var trees = 0;
        var emptyplaces = 0;
        while (y < mapSizeVertical)
        {
            if (map[x][y] == TileTypes.Tree)
            {
                trees++;
//              map[x][y] = TileTypes.EncounterTree;
            }
            else
            {
                emptyplaces++;
//              map[x][y] = TileTypes.EncounterEmpty;
            }
            x += xOffset;
            y += yOffset;
        }
        println("--> Encountered $trees trees on my way, just $emptyplaces empty places...");
        println("Max X: $x");
        return trees;
    }
}

fun main(args : Array<String>) {
    val map = Map("src/Day3Input");
    var slope1Trees = map.countTrees(1,1).toLong();
    val slope2Trees = map.countTrees(3,1).toLong();
    val slope3Trees = map.countTrees(5, 1).toLong();
    val slope4Trees = map.countTrees(7,1).toLong();
    val slope5Trees = map.countTrees(1, 2).toLong();
    println("Slope 1 Result: $slope1Trees");
    println("Slope 2 Result: $slope2Trees");
    println("Slope 3 Result: $slope3Trees");
    println("Slope 4 Result: $slope4Trees");
    println("Slope 5 Result: $slope5Trees");
    val result = slope1Trees * slope2Trees * slope3Trees * slope4Trees * slope5Trees;
    println("Total result of trees is $result");
}