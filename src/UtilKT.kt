import java.io.File

fun readFile(filename : String) : List<String> = File(filename).readLines();
