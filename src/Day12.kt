import kotlin.math.abs

class Day12 (filename : String){

    var posEast = 0;
    var posWest = 0;
    var posNorth = 0;
    var posSouth = 0;

    //0 = East, 90 = South, 180 = West, 270 = North
    var direction = 0;


    init{
        val input = readFile(filename)

        for (i in input){
            val command = i.substring(0,1)
            val value = i.substring(1).toInt()
            when (command) {
                "N" -> {
                    posNorth+=value;
                }
                "S" -> {
                    posSouth+=value;
                }
                "E" -> {
                    posEast+=value;
                }
                "W" -> {
                    posWest+=value;
                }
                "L" -> {
                    direction -= value;
                    if (direction < 0) {
                        direction += 360
                    };
                }
                "R" -> {
                    direction += value;
                    if (direction >= 360) {
                        direction -= 360
                    };
                }
                "F" -> {
                    when (direction) {
                        0 -> {
                            posEast+=value;
                        }
                        90 -> {
                            posSouth+=value;
                        }
                        180 -> {
                            posWest+=value;
                        }
                        270 -> {
                            posNorth+=value;
                        }
                    }
                }
            }
        }
        val manhattanDistance = abs(posSouth-posNorth) + abs(posEast-posWest)
        println("Manhattan Distance is $manhattanDistance")
    }

}
fun main (args : Array<String>){
    Day12("src/Day12Input").run{}
}
