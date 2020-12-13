import kotlinx.coroutines.delay
import kotlin.concurrent.thread
import kotlin.math.min

class Day13 (filename: String){
    val filename = filename;
    fun part1() {
        val input = readFile(filename)
        val departTimestamp = input[0].toInt()
        var result = 999999999;
        var resultMinID = 0;
        for (i in input[1].split(",")){
            if (i == "x") continue
            val busID = i.toInt()
            val mod = busID - departTimestamp % busID;
            result = min(mod, result)
            if (result == mod){
                resultMinID = busID;
            }
        }
        result *= resultMinID
        println("Result Part 1 is $result")
    }

    var foundResult = false;

    suspend fun part2() {

        val increments: Long = 100000000000

        var startTime: Long = 100000000000000
        foundResult = false;

        val threadList : MutableList<Thread> = mutableListOf();

        while (!foundResult)
            if (threadList.all { i -> i.state == Thread.State.TERMINATED }){
                for (i in 0..9) {

                    val thread = thread (start = true) {
                        part2Worker(startTime, startTime + increments)
                    };
                    delay(5) //without delay startTime gets a false value
                    startTime += increments
                    threadList.add(thread)
                }
            }

            delay(1000)
    }


    fun part2Worker(starttime :Long, endtime : Long) {
        println("Starting worker @ $starttime till $endtime")
        val input = readFile(filename)
        var departureTime : Long = starttime;
        val sortedListBusID = mutableListOf<Long>()
        val mapBusIDIndex = mutableMapOf<Long,Long>()
        val mapBusIDDepartTime = mutableMapOf<Long,Long>()
        for ((index, i) in input[1].split(",").withIndex()){
            if (i == "x") continue;
            val busID = i.toLong()
            sortedListBusID.add(busID)
            mapBusIDIndex[busID] = index.toLong()
            mapBusIDDepartTime[busID] = departureTime;
            for (i2 in departureTime .. departureTime+busID){
                if (i2 % busID == 0L){
                    mapBusIDDepartTime[busID] = i2
                }
            }
        }
        sortedListBusID.sortDescending()
        var found = false;
loop@   while (!found){
            found = true;
            val busID = sortedListBusID[0]!!
            val busIndex = mapBusIDIndex[busID]!!
            val lastDepartTime = mapBusIDDepartTime[busID]!! - busIndex
loopfor@    for (bus in sortedListBusID){
                val index = mapBusIDIndex[bus]!!
                val mod = (lastDepartTime + index) % bus
                if (mod != 0L){
                    found = false;
                    mapBusIDDepartTime[busID] = mapBusIDDepartTime[busID]!! + busID
                    break@loopfor
                }
            }
            if (lastDepartTime >= endtime) break;

            if (found){
                departureTime = lastDepartTime
                foundResult = true;
            }
        }
        if (foundResult){
            println("Earliest departure time is $departureTime") //Solution: 305068317272992
        }

    }
}

suspend fun main(args : Array<String>) {
    Day13("src/Day13Input").run { part1();part2() }

}