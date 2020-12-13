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