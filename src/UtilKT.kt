import java.io.File

fun readFile(filename : String) : List<String> = File(filename).readLines();

fun gcd (a : Long, b : Long) : Long {
    var a = a
    var b = b
    while (b != 0L) {
        var t = b
        b = a % b
        a = t
    }
    return a;
}

fun lcm (a : Long, b : Long) : Long {
    return a * b / gcd(a, b)
}

fun printArray(inputArray: Array<Array<Char>>) {
    println()

    for (y in 0 until inputArray.size){
        for (x in 0 until inputArray.size) {
            print("${inputArray[x][y]}")
        }
        println()
    }
}

fun flip(inputArray : Array<Array<Char>>) : Array<Array<Char>> {
    val length = inputArray.size
    val result = Array(length){Array(length){'.'} }


    for (y in 0 until length){
        for (x in 0 until length) {
            result[x][y] = inputArray[x][length-1-y]
        }
    }

    return result
}



fun copyArray(input : Array<Array<Char>>, dimension : Int) : Array<Array<Char>>{
    val result = Array(dimension){Array(dimension) {'.'} }

    for (y in 0 until dimension){
        for (x in 0 until dimension){
            result[x][y] = input[x][y]
        }
    }


    return result
}

fun rotate(steps : Int, inputArray : Array<Array<Char>>) : Array<Array<Char>>{
    val length = inputArray.size
    val result = Array(length){Array(length){'.'} }
    var input = copyArray(inputArray.copyOf(), length)
    for (i in 0 until 4-steps) {
        for (y in 0 until length){
            for (x in 0 until length) {
                result[x][y] = input[length-y-1][x]
            }
        }
        input = copyArray(result, length )
    }

    return result
}