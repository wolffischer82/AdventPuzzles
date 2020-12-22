
enum class Winner{
    player1,
    player2
}

class RecursiveGame () {
    val prevRoundsP1 = mutableSetOf<String>()
    val prevRoundsP2 = mutableSetOf<String>()
    fun playTheGame(deck1 : MutableList<Int>, deck2 : MutableList<Int>) : Winner{
        var round = 0
        while (deck1.size > 0 && deck2.size > 0){
            round++
            if (prevRoundsP1.contains(deck1.toString()) && prevRoundsP2.contains(deck2.toString())){
                return Winner.player1
            }
            prevRoundsP1.add(deck1.toString())
            prevRoundsP2.add(deck2.toString())

            var firstCardP1 = deck1[0]
            var firstCardP2 = deck2[0]

            if (firstCardP1 <= deck1.size-1 && firstCardP2 <= deck2.size-1){
                val newDeck1 = deck1.subList(1, firstCardP1+1).toMutableList()
                val newDeck2 = deck2.subList(1, firstCardP2+1).toMutableList()
                val result = RecursiveGame().playTheGame(newDeck1, newDeck2)

                if (result == Winner.player1){
                    deck1.add(deck1[0])
                    deck1.add(deck2[0])
                } else {
                    deck2.add(deck2[0])
                    deck2.add(deck1[0])
                }
            }
            else {
                if (deck1[0] > deck2[0]){
                    deck1.add(deck1[0])
                    deck1.add(deck2[0])
                }
                else{
                    deck2.add(deck2[0])
                    deck2.add(deck1[0])
                }
            }

            deck1.removeAt(0)
            deck2.removeAt(0)


        }
        if (deck1.size > 0) return Winner.player1
        return Winner.player2
    }

}

class Day22 (filename : String){
    val filename = filename
    val playersDecks = mutableListOf<MutableList<Int>>()

    fun readInput(){
        val input = readFile(filename)
        playersDecks.clear()
        var playerID = 0;
        for (l in input){
            if (l == "") continue
            if (l.startsWith("Player")){
                playersDecks.add(mutableListOf())
                playerID = playersDecks.size-1
            }
            else{
                playersDecks[playerID].add(l.toInt())
            }
        }
    }


    fun playTheGame(deck1 : MutableList<Int>, deck2 : MutableList<Int>) : Winner{
        while (deck1.size > 0 && deck2.size > 0){
            if (deck1[0] > deck2[0]){
                deck1.add(deck1[0])
                deck1.add(deck2[0])
            }
            else{
                deck2.add(deck2[0])
                deck2.add(deck1[0])
            }
            deck1.removeAt(0)
            deck2.removeAt(0)
        }
        if (deck1.size > 0) return Winner.player1
        return Winner.player2
    }

    fun calculateScore(deck1 : List<Int>, deck2 : List<Int>) : Int{
        var result = 0

        var deck = deck1
        if (deck2.size > 1) {
            deck = deck2
        }

        for (i in 0 until deck.size){
            result += deck[deck.size-1-i]*(i+1)
        }
        return result
    }


    fun playRecursiveGame(deck1 : MutableList<Int>, deck2 : MutableList<Int>){

    }

    fun part1(){
        readInput()
        playTheGame(playersDecks[0], playersDecks[1])
        val result = calculateScore(playersDecks[0], playersDecks[1])
        println("Result Part 1 is $result")
    }

    fun part2(){
        readInput()
        val recGame = RecursiveGame().apply { playTheGame(playersDecks[0], playersDecks[1]) }
        val result = calculateScore(playersDecks[0], playersDecks[1])
        println("Result Part 2 is $result")
    }

}

fun main(args : Array<String>){
    Day22("src/Day22Input").run { part1(); part2() }
}