package usc.carlos.almuina.varela

import java.util.*


data class Solution(
        private val itinerary: Array<Int>
) {

    fun cost(citiesDistances: Matrix): Int {
        var total = 0
        var previous = 0
        require(citiesDistances.size == MAX_CITIES)
        itinerary.forEach { i ->
            total += citiesDistances[previous, i]
            previous = i
        }
        return total + citiesDistances[previous, 0]
    }

    fun betterThan(other: Solution, matrix: Matrix): Boolean = this.cost(matrix) < other.cost(matrix)

    fun betterThan(other: Int, matrix: Matrix): Boolean = this.cost(matrix) < other

    fun worseEqualThan(other: Solution, matrix: Matrix) = !betterThan(other, matrix)
    fun toString(matrix: Matrix): String = "\tRECORRIDO: " + itinerary.joinToString(" ") { "$it" } + " \n\tCOSTE (km): ${this.cost(matrix)}"

    fun stringify(citiesDistances: Matrix) = "${Arrays.toString(itinerary)}; ${cost(citiesDistances)}km"

    private fun newNeighbour(P: Pair<Int, Int>): Solution {
        // Cambiamos el operador de generación de vecinos y ordenamos al revés todos los operadores entre los pares sacados
        val (end, start) = P
        val init = this.itinerary.copyOfRange(0, start)
        val mid = this.itinerary.copyOfRange(start, end)
        val last = this.itinerary.copyOfRange(end, this.itinerary.size)

        if ((P.second + P.first) % 2 == 0)
            return Solution(init + (mid).reversedArray() + last)

        return Solution((init + (mid.reversedArray() + last).reversedArray()))

    }

    // Generación de todos los pares de números.
    private fun generatePairs(tabooList: TabooList): List<Pair<Int, Int>> =
            (0 until CITIES).flatMap({ i -> (0 until CITIES).map { j -> i to j } })
                    .filter { (a, b) -> a > b }.filter { (a, b) -> !tabooList.check(Pair(a, b)) }

    private fun getBest(matrix: Matrix, tabooList: TabooList): Pair<Solution?, Pair<Int, Int>?> {
        var best: Solution? = null
        var bestCost = Int.MAX_VALUE
        var p: Pair<Int, Int>? = null

        val listOfSolutions = generatePairs(tabooList).parallelStream()
                .map { this.newNeighbour(it) to it }

        listOfSolutions.forEach { (prime, pair) ->
            if (prime.betterThan(bestCost, matrix)) {
                bestCost = prime.cost(matrix)
                best = prime
                p = pair
            }

        }
        if (best == null) throw Exception("There was no neighbourhood to explore.")
        return Pair(best, p)
    }

    fun tabooSearch(matrix: Matrix): Solution {
        var current: Pair<Solution?, Pair<Int, Int>?>
        var best = this
        var previous = this
        var bestCost = Int.MAX_VALUE
        val taboo = TabooList()
        var noImprovement = 0
        var resets = 0
        var bestSolutionIteration = 0

        repeat(ITERATIONS) {
            if (noImprovement >= NO_IMPROVEMENT_TRESHOLD) {
                noImprovement = 0
                resets++
                previous = best
                if (resets % TABOO_CLEAR == 0) {
                    taboo.clear()
                }
                println("\n***************\nREINICIO: $resets\n***************")
            }

            current = previous.getBest(matrix, taboo)
            taboo.insert(current.second!!) // Introducimos el par en la lista

            if (bestCost > current.first!!.cost(matrix)) {
                noImprovement = 0
                best = current.first!!
                bestCost = best.cost(matrix)
                bestSolutionIteration = it
            } else {
                noImprovement++
            }
            previous = current.first!!
            println(iterationString(it, current.second, previous, noImprovement, matrix, taboo))
        }

        println("\n\nMEJOR SOLUCION: \n${best.toString(matrix)}\n\tITERACION: ${bestSolutionIteration + 1}")
        return best
    }

    private fun iterationString(iteration: Int, p: Pair<Int, Int>?, solution: Solution, improvement: Int, matrix: Matrix, tabooList: TabooList): String =
            "\nITERACION: ${iteration + 1}\n\tINTERCAMBIO: $p\n" + solution.toString(matrix) + "\n\tITERACIONES SIN MEJORA: $improvement\n\tLISTA TABU:\n" + tabooList.stringify()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Solution

        if (!Arrays.equals(itinerary, other.itinerary)) return false

        return true
    }

    override fun hashCode(): Int = Arrays.hashCode(itinerary)


    companion object {
        fun init(random: Random): Solution {
            val newItinerary = Array(CITIES) { 0 }

            repeat(CITIES) {
                var aux = (1 + random.next())
                while (aux in newItinerary) {
                    aux = (aux + 1) % MAX_CITIES
                }
                newItinerary[it] = aux
            }
            return (Solution(newItinerary))
        }


        fun greedyInit(matrix: Matrix): Solution {
            val newItinerary = Array(CITIES) { -1 }
            val visited = Array(MAX_CITIES) { false }
            var previous = 0
            var city = -1
            var min: Int
//Empezando en la ciudad 0, buscar la siguiente ciudad más cercana (que no haya sido ya visitada, y que no sea ella misma) y quitarla de la pool de soluciones. Repeat.
            repeat(CITIES) {
                min = Int.MAX_VALUE
                (1..CITIES)
                        .filter { !visited[it] }
                        .filter { it != previous }
                        .forEach {
                            val cost = matrix[previous, it]
                            if (cost < min) {
                                min = cost
                                city = it
                            }
                        }
                visited[city] = true
                previous = city
                newItinerary[it] = city
            }

            return (Solution(newItinerary))
        }


    }
}