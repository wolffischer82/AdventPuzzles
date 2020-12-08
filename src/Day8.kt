class Day8(filename : String) {
    var input = listOf<String>();
    var codeExecutionMap = mutableMapOf<Int, String>();
    var acc = 0;

    init {
        input = readFile(filename);

    }

    fun convertInt(input : String) : Int{
        val accVal = input.substring(1).toInt();
        if (input[0] == '-') {
            return accVal * -1;
        }
        return  accVal;
    }

    fun execute()
    {
        var currentLine = 0;
        while (!codeExecutionMap.containsKey(currentLine))
        {
            codeExecutionMap[currentLine] = input[currentLine];
            val splitCommand = input[currentLine].split(" ");
            when {
                splitCommand[0] == "nop" -> {currentLine++}
                splitCommand[0] == "acc" -> {
                    acc += convertInt(splitCommand[1]);
                    currentLine++;
                }
                splitCommand[0] == "jmp" -> {
                    currentLine += convertInt(splitCommand[1]);
                }
            }
        }
        println ("Last acc value is $acc");
    }

    fun executePart2() {

    }
}

fun main(args : Array<String>)
{
    val day8 = Day8("src/Day8Input");
    day8.execute();
    day8.executePart2();

}