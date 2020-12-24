package `2020`

import readFile

class Food(ingredients : Set<String>, allergens : Set<String>){
    val ingredients = ingredients
    val allergens = allergens
}

class Day21 (filename : String){
    val filename = filename
    fun part1(){
        val foods = mutableListOf<Food>()
        val input = readFile(filename)
        for (l in input) {
            val ingredientsAllergens = l.split(" (")
            var ingredients = ingredientsAllergens[0].split(" ")
            var allergens = ingredientsAllergens[1].split(" ")
            allergens = allergens.subList(1, allergens.size).toMutableList()
            for ((i, v) in allergens.withIndex()) {
                allergens[i] = v.replace(",", "").replace(")", "")
            }
            foods.add(Food(ingredients.toSet(), allergens.toSet()))
        }

        val allIngredients = mutableSetOf<String>()
        val allergenMap = mutableMapOf<String, MutableSet<String>>()
        for (f in foods){
            for (a in f.allergens){
                allergenMap.putIfAbsent(a, mutableSetOf())
                allIngredients.addAll(f.ingredients)
                val allergenIngredients = allergenMap[a]!!
                if (allergenIngredients.isEmpty()){
                    allergenIngredients += f.ingredients
                } else {
                    val newset = allergenIngredients.intersect(f.ingredients)
                    allergenIngredients.clear()
                    allergenIngredients.addAll(newset)
                }
            }
        }
        for (f in allergenMap.values){
            allIngredients.removeAll(f)
        }

        var appearanceCounter = 0
        for (i in allIngredients){
            for (f in foods){
                if (i in f.ingredients){
                    appearanceCounter++
                }
            }
        }
        println("Harmless ingredients appear in $appearanceCounter foods!")

        val allergenIngredients = allergenMap.values.toMutableList()

 wh@    while (!allergenIngredients.all { it.size == 1 }){
           allergenIngredients.sortBy { it.size }
           for (ing in allergenIngredients){
                for (ing2 in allergenIngredients){
                    if (ing == ing2) continue
                    if (ing.size > 1) continue@wh
                    ing2.removeAll(ing)
                }
            }
        }

        val allergenList = allergenMap.keys.sorted()
        var ingredientString = ""
        for (a in allergenList){
            val v = allergenMap[a]!!
            for (s in v){
                ingredientString += s + ","
            }
        }
        ingredientString = ingredientString.substring(0, ingredientString.length-1)
        println ("Ingredientstring is: ")
        println(ingredientString)

    }
}
fun main(args : Array<String>){
    Day21("data/2020/Day21Input").run { part1() }
}