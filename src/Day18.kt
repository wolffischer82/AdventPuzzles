import kotlin.math.max

enum class ExpressionNodeType {
    Unknown,
    Operator,
    Value,
    Evaluated
}

class ExpressionNode(expression : List<String>){
    val expressionList = expression
    var nodeType = ExpressionNodeType.Unknown
    var index = 0
    var operator = ""
    var value = -1
    var parentheses = 0

    var parent : ExpressionNode? = null
    var leftChild : ExpressionNode? = null
    var rightChild : ExpressionNode? = null
//    var children : MutableList<ExpressionNode>? = mutableListOf<ExpressionNode>()


    fun parseExpression2() : ExpressionNode? {
        val exprNodeList = mutableListOf<ExpressionNode>()
        var ptCounter = 0;

        var operators = listOf('+', '*')
        var maxParentheses = 0
        for (i in 0..expressionList.size-1) {
            var elem = expressionList[i]
            var nodeType = ExpressionNodeType.Unknown
            var valueString = ""
            var lowerPTCounter = false;
            for (p in elem){
                if (p == '(') ptCounter++
                else if (p == ')') lowerPTCounter=true
                else if (!operators.contains(p)) {
                    valueString += p.toString()
                    nodeType = ExpressionNodeType.Value
                }
                else {
                    nodeType = ExpressionNodeType.Operator
                    valueString = p.toString();
                }
            }

            val node = ExpressionNode(expressionList)
            maxParentheses = max(maxParentheses, ptCounter)
            node.nodeType = nodeType
            node.parentheses = ptCounter
            node.index = i
            when (nodeType) {
                ExpressionNodeType.Operator -> {
                    node.operator = valueString
                }
                ExpressionNodeType.Value -> {
                    node.value = valueString.toInt()
                }
            }
            exprNodeList.add(node)
            if (lowerPTCounter) ptCounter--
        }


        val operatorNodesList = mutableListOf<ExpressionNode>()

        //join all on the same parentheses level
        for (i in 0..exprNodeList.size-1){
            val node = exprNodeList[i]
            if (node.nodeType == ExpressionNodeType.Operator) {
                val nodeLeft = searchParentlessNode(-1, i, exprNodeList, node.parentheses)
                if (nodeLeft != null && node.leftChild == null){
                    nodeLeft!!.parent = node;
                    node.leftChild = nodeLeft
                }
                val nodeRight = searchParentlessNode(1, i, exprNodeList, node.parentheses)
                if (nodeRight != null && node.rightChild == null){
                    nodeRight!!.parent = node;
                    node.rightChild = nodeRight
                }
                operatorNodesList.add(node)
            }
        }
        removeCompleteNodes(operatorNodesList)
        while (operatorNodesList.size > 1) {

            for (i in 0..operatorNodesList.size-1){
                val node = operatorNodesList[i]
                if (node.parentheses < maxParentheses) continue

                if (node.parent != null && node.leftChild != null && node.rightChild != null) continue
                var nodeLeft : ExpressionNode? = null
                if (i > 0) {
                    nodeLeft = operatorNodesList[i-1]
                }
                var nodeRight : ExpressionNode? = null
                if (i < operatorNodesList.size-1) {
                    nodeRight = operatorNodesList[i+1]
                }

                if (nodeLeft != null && node.parentheses == nodeLeft.parentheses) {
                    if (node.parent == null && nodeLeft.rightChild == null){
                        node.parent = nodeLeft
                        nodeLeft.rightChild = node
                    } else if (node.leftChild == null && nodeLeft.parent == null){
                        node.leftChild = nodeLeft
                        nodeLeft.parent = node
                    }
                } else if (nodeRight != null && node.parentheses == nodeRight.parentheses){
                    if (node.parent == null && nodeRight.leftChild == null){
                        node.parent = nodeRight
                        nodeRight.leftChild = node
                    }
                    else if (node.rightChild == null && nodeRight.parent == null){
                        node.rightChild = nodeRight
                        nodeRight.parent = node
                    }
                } else {
                    if (nodeLeft != null && node.parent == null && nodeLeft.rightChild == null
                        && node.parentheses == nodeLeft.parentheses+1){
                        node.parent = nodeLeft
                        nodeLeft.rightChild = node
                    } else if (nodeLeft != null && node.leftChild == null && nodeLeft.parent == null
                        && node.parentheses == nodeLeft.parentheses+1){
                        node.leftChild = nodeLeft
                        nodeLeft.parent = node
                    } else if (nodeRight != null && node.parent == null && nodeRight.leftChild == null
                        && node.parentheses == nodeRight.parentheses+1){
                        node.parent = nodeRight
                        nodeRight.leftChild = node
                    }
                    else if (nodeRight != null && node.rightChild == null && nodeRight.parent == null
                        && node.parentheses == nodeRight.parentheses+1){
                        node.rightChild = nodeRight
                        nodeRight.parent = node
                    }
                }
            }

            removeCompleteNodes(operatorNodesList)
            maxParentheses--
        }

        return operatorNodesList[0]
    }

    fun removeCompleteNodes(list : MutableList<ExpressionNode>){
        var removeCompleteNodes = mutableListOf<ExpressionNode>()
        for (node in list){
            if (node.parent != null && node.leftChild != null && node.rightChild != null) {
                removeCompleteNodes.add(node)
            }
        }
        list.removeAll(removeCompleteNodes)

    }

    fun searchParentlessNode(Increment : Int,
                             Index : Int,
                             exprList : List<ExpressionNode>,
                             parentheses : Int) : ExpressionNode?{
        var i = Index
        var result : ExpressionNode? = null
        while (i+Increment >= 0 && i+Increment <= exprList.size-1) {
            i += Increment
            val node = exprList[i]
            if (node.parent == null) {
                if (node.parentheses == parentheses) {
                    return node
                } else {
                    return null
                }
            }

        }
        return null
    }

    fun evaluateExpression() : Long {
        var result = 0L
        if (nodeType == ExpressionNodeType.Value) {
            result = value.toLong()
        }
        else if (nodeType == ExpressionNodeType.Operator) {
            var r1 = leftChild!!.evaluateExpression()
            var r2 = rightChild!!.evaluateExpression()
            if (operator == "+") {
                result = r1 + r2
            } else if (operator == "*") {
                result = r1 * r2
            }
        }

        return result
    }

}


class Day18 (filename : String) {
    val filename = filename

    fun part1(){
        val input = readFile(filename)
        var result = 0L;
        for (i in input) {
            if (i.startsWith("#")) continue
            val expressionResult= i.split(" = ")
            val expr = ExpressionNode(expressionResult[0].split(" ")).parseExpression2()
            val res = expr!!.evaluateExpression()

            println("${expressionResult[0]} = $res")
            if (expressionResult.size == 2 && res != expressionResult[1].toLong()){
                println("\t Error ! Should have been ${expressionResult[1]}")
            }
            result += res

        }
        println("Sum of all expressions: $result")
    }

    fun part2(){

    }
}
fun main ( args : Array<String>){
    //Day18("src/Day18Input").run { part1(); part2() }
    Day18("src/Day18InputTest").run { part1(); part2() }

}