package `2020`

class Day25 () : TemplateDay(25, 2020) {
    fun transformSubject(subjectNumber : Long, loopSize : Long) : Long{
        var result = 1L
        for (loop in 1..loopSize) {
            result *= subjectNumber
            result = result % 20201227
        }
        return result
    }

    override fun part1() {

        val cardKey = input?.get(0)?.toLong()
        val doorKey = input?.get(1)?.toLong()

        var result = 1L
        var loop = 0L
        while(result != cardKey) {
            loop++
            result *= 7
            result = result % 20201227
        }

        val encryptionKey = transformSubject(doorKey!!, loop)
        println ("Encryptionkey is $encryptionKey")

    }

    override fun part2() {
        super.part2()
    }
}

fun main (args : Array<String>){
    Day25().run { part1(); part2() }
}
