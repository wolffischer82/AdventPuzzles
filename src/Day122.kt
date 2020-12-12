import kotlin.math.abs

class Day122 (filename : String){

    var waypointX = 0;
    var waypointY = 0;

    var shipEast = 0;
    var shipWest = 0;
    var shipNorth = 0;
    var shipSouth = 0;

    init{
        val input = readFile(filename)
        waypointX = 10;
        waypointY = 1;
        for (i in input){
            val command = i.substring(0,1)
            val value = i.substring(1).toInt()
            when (command) {
                "N" -> {
                    waypointY+=value;
                }
                "S" -> {
                    waypointY-=value;
                }
                "E" -> {
                    waypointX+=value;
                }
                "W" -> {
                    waypointX-=value;
                }
                "L" -> {
                    for (i in 0..value/90-1){
                        val t = waypointX;
                        waypointX = waypointY;
                        waypointY = t;
                        waypointX *= -1;
                    }
                }
                "R" -> {
                    for (i in 0..value/90-1){
                        val t = waypointX;
                        waypointX = waypointY;
                        waypointY = t;
                        waypointY *= -1;
                    }
                }
                "F" -> {
                    val yPos = waypointY*value;
                    val xPos = waypointX*value;
                    if (yPos > 0){
                        shipNorth+= abs(yPos);
                    } else {
                        shipSouth+= abs(yPos);
                    }
                    if (xPos > 0) {
                        shipEast += abs(xPos);
                    } else {
                        shipWest += abs(xPos);
                    }
                }
            }
            println("Command $i")
            println("\tWaypoint: Y$waypointY X$waypointX")
            println("\tShip: N$shipNorth E$shipEast S$shipSouth W$shipWest")
        }
        val manhattanDistance = abs(shipSouth-shipNorth) + abs(shipEast-shipWest)
        println("Manhattan Distance is $manhattanDistance")
    }

}
fun main (args : Array<String>){
    Day122("src/Day12Input").run{}
}
