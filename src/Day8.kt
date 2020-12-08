class Day8(filename : String) {
    var input = listOf<String>();
    var codeExecutionMap = mutableMapOf<Int, String>();
    var successfulFinished = false;
    var acc = 0;
    init {
        input = readFile(filename).toMutableList();

    }

    fun execute(){
        execute(input);
        println ("Last acc value is $acc");
    }

    fun execute(code : List<String>)
    {
        acc = 0;
        var currentLine = 0;
        successfulFinished = false;
        codeExecutionMap.clear();
        while (currentLine < code.size && !codeExecutionMap.containsKey(currentLine))
        {
            codeExecutionMap[currentLine] = code[currentLine];
            val splitCommand = code[currentLine].split(" ");
            when {
                splitCommand[0] == "nop" -> {currentLine++}
                splitCommand[0] == "acc" -> {
                    acc += splitCommand[1].toInt();
                    currentLine++;
                }
                splitCommand[0] == "jmp" -> {
                    currentLine += splitCommand[1].toInt();
                }
            }
        }
        if (currentLine == code.size)
        {
            successfulFinished = true;
        }
    }

    fun executePart2() {
        for (i in 0..input.size-1)
        {
            var copy = input.toMutableList();
            if (input[i].contains("nop")){
                copy[i] = input[i].replace("nop", "jmp");
            }
            if (input[i].contains("jmp")){
                copy[i] = input[i].replace("jmp", "nop");
            }
            execute(copy);
            if (successfulFinished) {
                println("Successful finished with Acc value $acc");
                break;
            }
        }
    }
}

fun main(args : Array<String>)
{
    val day8 = Day8("src/Day8Input");
    day8.execute();
    day8.executePart2();
}