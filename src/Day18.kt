import java.util.*
import kotlin.math.max

//https://github.com/jbush7401/AdventOfCode/blob/master/AdventOfCode/2020/Day18.cs
class Operation{
    val numbers = mutableListOf<Long>()
    val operators = mutableListOf<Char>()

    fun calculate() : Long {
        var total = 0L
        for (i in 0..numbers.size-1){
            if (operators[i] == '+'){
                total += numbers[i]
            } else {
                total *= numbers[i]
            }
        }
        return total
    }
    fun calculateMath() : Long{
        var total = 1L
        var addResult= 0L
        var additionalResultList = mutableListOf<Long>()
        for (i in 0..numbers.size-1){
            if (operators[i] == '+') {
                addResult += numbers[i]
            }
            else {
                additionalResultList.add(addResult)
                addResult = 0
                addResult+=numbers[i]
            }
        }
        additionalResultList.add(addResult)
        for (i in additionalResultList){
            total *= i
        }
        return total

    }
}


class Day18 (filename : String) {
    val filename = filename
    val operationsStack = Stack<Operation>()

    fun part1(){
        val input = readFile(filename)
        var result = 0L;

        for (i in input){
            val splitLine = i.split(" = ")
            operationsStack.push(Operation())
            for (c in splitLine[0].replace(" ", "")){
                when (c) {
                    '+'  -> {
                        operationsStack.peek().operators.add(c)
                    }
                    '*'  -> {
                        operationsStack.peek().operators.add(c)
                    }
                    '(' -> {
                        operationsStack.push(Operation())
                    }
                    ')' -> {
                        var final = operationsStack.peek().calculate()
                        operationsStack.pop()
                        operationsStack.peek().numbers.add(final)
                        if (operationsStack.peek().operators.size == 0) {
                            operationsStack.peek().operators.add('+')
                        }
                    }
                    else -> {
                        operationsStack.peek().numbers.add(c.toString().toLong())
                        if (operationsStack.peek().operators.size == 0) {
                            operationsStack.peek().operators.add('+')
                        }
                    }
                }
            }
            val res = operationsStack.pop().calculate()
            if (splitLine.size > 1){
                if (res != splitLine[1].toLong()){
                    println("Error: ${splitLine[0]} should be ${splitLine[1]}, was $res")
                }
            }
            result += res
        }

        println("Sum of all expressions: $result")
    }

    fun part2(){
        val input = readFile(filename)
        var result = 0L;

        for (i in input){
            val splitLine = i.split(" = ")
            operationsStack.push(Operation())
            for (c in splitLine[0].replace(" ", "")){
                when (c) {
                    '+'  -> {
                        operationsStack.peek().operators.add(c)
                    }
                    '*'  -> {
                        operationsStack.peek().operators.add(c)
                    }
                    '(' -> {
                        operationsStack.push(Operation())
                    }
                    ')' -> {
                        var final = operationsStack.peek().calculateMath()
                        operationsStack.pop()
                        operationsStack.peek().numbers.add(final)
                        if (operationsStack.peek().operators.size == 0) {
                            operationsStack.peek().operators.add('+')
                        }
                    }
                    else -> {
                        operationsStack.peek().numbers.add(c.toString().toLong())
                        if (operationsStack.peek().operators.size == 0) {
                            operationsStack.peek().operators.add('+')
                        }
                    }
                }
            }
            val res = operationsStack.pop().calculateMath()
            if (splitLine.size > 1){
                if (res != splitLine[1].toLong()){
                    println("Error: ${splitLine[0]} should be ${splitLine[1]}, was $res")
                }
            }
            result += res
        }

        println("Sum of all expressions: $result")
    }
}
fun main ( args : Array<String>){
    Day18("src/Day18Input").run { part1(); part2() }
    //Day18("src/Day18InputTest").run { part1(); part2() }

}