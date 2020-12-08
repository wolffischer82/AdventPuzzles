class Day6 (filename : String){
    val groupsPart1 = mutableListOf<MutableSet<Char>>();
    val groupsPart2 = mutableListOf<MutableSet<Char>>();

    init {
        val input = readFile(filename);

        var createdNewSet = false;
        for (groupString in input)
        {
            if (groupString == "" || groupsPart1.size == 0)
            {
                groupsPart1.add(mutableSetOf());
                groupsPart2.add(mutableSetOf());
                createdNewSet = true;
            }

            groupsPart1[groupsPart1.size-1].addAll(groupString.toList());
            if (createdNewSet && groupString != "")
            {
                createdNewSet = false;
                groupsPart2[groupsPart2.size-1].addAll(groupString.toList());
            }
            else {
                val newSet = groupsPart2[groupsPart2.size - 1].intersect(groupString.toList());
                groupsPart2[groupsPart2.size - 1] = newSet.toMutableSet();
            }
        }

        var resultPart1 = 0;
        for (group in groupsPart1)
        {
            resultPart1 += group.size;
        }
        println("Number of correct answers part 1: $resultPart1");

        var resultPart2 = 0;
        for (group in groupsPart2)
        {
            resultPart2 += group.size;
        }
        println("Number of correct answers part 2: $resultPart2");

    }
}
fun main (args : Array<String>)
{
    Day6("src/Day6Input");
}