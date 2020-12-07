import java.lang.NumberFormatException

class BagNode(color : String){
    companion object{
        private var colorToNodeMap = mutableMapOf<String, BagNode>();
    }

    private var nodeRelationContains = mutableMapOf<BagNode, Int>();
    var nodeRelationContainedIn = mutableListOf<BagNode>();

    var color = "";
    init {
        this.color = color;
    }

    public fun addNode(node : BagNode?, num : Int){
        colorToNodeMap[node!!.color] = node;
        nodeRelationContains[node] = num;
        node.addNodeContainedIn(this);
    }

    fun addNodeContainedIn(node : BagNode){
        nodeRelationContainedIn.add(node);
    }

}

class Day7 (filename : String) {
    val nodeMap = mutableMapOf<String, BagNode>();
    init {
        val input = readFile(filename);

        for (rule in input)
        {
            val colorSplit = rule.split(" bags contain ");

            if (!nodeMap.containsKey(colorSplit[0]))
            {
                nodeMap[colorSplit[0]] = BagNode(colorSplit[0]);
            }
            val currentNode = nodeMap[colorSplit[0]];

            var containedBagsStrings = mutableListOf<String>();
            for (containedBag in colorSplit[1].split(", "))
            {
                var cleanedBagInfo = containedBag.replace(".", "");
                cleanedBagInfo = cleanedBagInfo.replace("bags", "").replace("bag", "");
                var amount = 0;
                try {
                    amount = cleanedBagInfo.substring(0,1).toInt();
                }
                catch (e : NumberFormatException){
                    println("Probably no other bags -> breaking!");
                    continue;
                }
                val bagName = cleanedBagInfo.substring(2).trim();
                if (!nodeMap.containsKey(bagName))
                {
                    nodeMap[bagName] = BagNode(bagName);
                }
                currentNode!!.addNode(nodeMap[bagName], amount);
            }
        }
    }
    fun searchBagsContaining(color : String){
        val bagNode = nodeMap[color];
        var nodeList = mutableListOf<BagNode>();
        var index = 0;
        var currentNode = bagNode;
        while (currentNode != null)
        {
            nodeList.addAll(currentNode.nodeRelationContainedIn);

            if (nodeList.size > index){
                currentNode = nodeList[index];
                index++;
            }
            else {
                currentNode = null;
            }
        }
        val resultSet = mutableSetOf<BagNode>();
        resultSet.addAll(nodeList);

        println("Found a total of ${resultSet.size} bags containing $color");
    }
}
fun main (args : Array<String>){
    val bagDay = Day7("src/Day7Input");
    bagDay.searchBagsContaining("shiny gold");
}