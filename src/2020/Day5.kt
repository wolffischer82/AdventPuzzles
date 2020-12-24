package `2020`

import readFile
import kotlin.math.max

class Day5 (filename : String){
    init {
        val input = readFile(filename);
        var highscore = 0;
        val seatIDsList = mutableListOf<Int>()

        for (line in input) {
            var rows = 0..127;
            var seats = 0..7;
            var row = 0;
            var seat = 0;
            for (c in line.toCharArray()) {
                var deltaRowsHalf = (rows.last - rows.first) / 2;
                var deltaSeatsHalf = (seats.last - seats.first) / 2;
                when {
                    c == 'F' -> {
                        rows = rows.first..rows.first + deltaRowsHalf
                        if (deltaRowsHalf == 0){
                            row = rows.first;
                        }

                    }
                    c == 'B' -> {
                        rows = rows.first + deltaRowsHalf + 1..rows.last
                        if (deltaRowsHalf == 0){
                            row = rows.last;
                        }
                    }
                    c == 'L' -> {
                        seats = seats.first..seats.first + deltaSeatsHalf
                        if (deltaSeatsHalf == 0){
                            seat = seats.first;
                        }
                    }
                    c == 'R' -> {
                        seats = seats.first + deltaSeatsHalf + 1 ..seats.last
                        if (deltaSeatsHalf == 0) {
                            seat = seats.last;
                        }
                    }
                }
            }
            val score = row * 8 + seat;
            seatIDsList.add(score);

            highscore = max(highscore, score);
            println("Remaining row: $row");
            println("Remaining seat: $seat");
        }
        val seatIDsListSorted = seatIDsList.sortedDescending().reversed();
        val completeSeatIDList = (seatIDsListSorted[0]..seatIDsListSorted[seatIDsListSorted.size-1]).toMutableList();
        completeSeatIDList.removeAll(seatIDsListSorted);

        println("Highscore: $highscore")
        println("My seat: ${completeSeatIDList[0]}");
    }

}

fun main (args : Array<String>){
    Day5("data/2020/Day5Input");
}