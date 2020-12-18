import kotlin.reflect.KFunction
//https://gist.github.com/erikhuizinga/d2ca2b501864df219fd7f25e4dd000a4

typealias CartesianProduct = Set<List<Int>>

/**
 * Create the cartesian product of any number of sets of any size. Useful for parameterized tests
 * to generate a large parameter space with little code. Note that any type information is lost, as
 * the returned set contains list of any combination of types in the input set.
 *
 * @param a The first set.
 * @param b The second set.
 * @param sets Any additional sets.
 */
fun cartesianProduct(a: Set<Int>, b: Set<Int>, vararg sets: Set<Int>): CartesianProduct =
    (setOf(a, b).plus(sets))
        .fold(listOf(listOf<Int>())) { acc, set ->
            acc.flatMap { list -> set.map { element -> list + element } }
        }
        .toSet()

/**
 * Transform elements of a cartesian product.
 */
fun <T> CartesianProduct.map(transform: KFunction<T>) = map { transform.call(*it.toTypedArray()) }