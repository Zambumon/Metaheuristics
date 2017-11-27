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
    fun toString(matrix: Matrix): String {
        return "\tRECORRIDO: " + itinerary.joinToString(" ") { "$it" } + " \n\tCOSTE (km): ${this.cost(matrix)}"
    }


    fun stringify(citiesDistances: Matrix) = "${Arrays.toString(itinerary)}; ${cost(citiesDistances)}km"

    private fun newNeighbour(P: Pair<Int, Int>): Solution {
        val neighbour = Solution(this.itinerary.clone())
        neighbour.itinerary[P.first] = itinerary[P.second]
        neighbour.itinerary[P.second] = itinerary[P.first]
        return neighbour
    }

    private fun getBest(matrix: Matrix, tabooList: TabooList): Pair<Solution?, Pair<Int, Int>?> {
        var best: Solution? = null
        var bestCost = Int.MAX_VALUE
        var p: Pair<Int, Int>? = null
        for (i in 0 until CITIES) {
            (0..CITIES - 2)
                    .filter { i > it }
                    .filter { !tabooList.check(Pair(i, it)) }
                    .forEach {
                        val prime = this.newNeighbour(Pair(i, it))
                        if (prime.betterThan(bestCost, matrix)) {
                            bestCost = prime.cost(matrix)
                            best = prime
                            p = Pair(i, it)
                        }
                    }
        }
        if (best == null) throw Exception("There was no neighbourhood to explore.")

        tabooList.insert(p!!)
        return Pair(best, p)
    }

    fun tabooSearch(matrix: Matrix): Solution {
        var currentSolution: Pair<Solution?, Pair<Int, Int>?>
        var best = this
        var previous = this
        var bestCost = Int.MAX_VALUE
        val taboo = TabooList()
        var iterationsWithoutImprovement = 0
        var resets = 0
        var bestSolutionIteration = 0
        repeat(ITERATIONS) {
            if (iterationsWithoutImprovement >= TENANCY) {
                taboo.clear()
                iterationsWithoutImprovement = 0
                resets++
                previous = best
                println("\n***************\nREINICIO: $resets\n***************")
            }

            currentSolution = previous.getBest(matrix, taboo)


            if (bestCost > currentSolution.first!!.cost(matrix)) {
                iterationsWithoutImprovement = 0
                best = currentSolution.first!!
                bestCost = best.cost(matrix)
                bestSolutionIteration = it
            } else
                iterationsWithoutImprovement++

            previous = currentSolution.first!!

            println("\nITERACION: ${it+1}\n\tINTERCAMBIO: ${currentSolution.second}")
            println(previous.toString(matrix))
            println("\tITERACIONES SIN MEJORA: $iterationsWithoutImprovement\n\tLISTA TABU:\n" + taboo.stringify())
        }

        println("\n\nMEJOR SOLUCION: \n${best.toString(matrix)}\n\tITERACION: ${bestSolutionIteration+1}")
        return best
    }


    fun getBestFirstNeighbour(matrix: Matrix, random: Random): Solution {
        val neighbourhoodSize = (MAX_CITIES - 1) * (MAX_CITIES - 2) / 2
        var prime: Solution
        var neighbourIndex = 0
        val explored = Matrix.emptyMatrix(itinerary.size)

        do {
            var (rand1, rand2) = random.nextPair()
            while (rand1 == rand2 || explored[rand1, rand2] != 0) {
                if (rand2 == rand1) {
                    rand1 = (rand1 + 1) % CITIES
                    rand2 = 0
                    if (rand1 == 0)
                        rand1 = 1
                } else {
                    rand2++
                }
            }
            prime = this.newNeighbour(Pair(rand1, rand2))
            explored[rand1, rand2] = 1
            println("\tVECINO V_$neighbourIndex -> Intercambio: ($rand1, $rand2); ${prime.stringify(matrix)}")
            neighbourIndex++
        } while (!prime.betterThan(this, matrix) && neighbourIndex < neighbourhoodSize)

        return prime
    }

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

            for (i in 0 until CITIES) {
                var aux = (1 + random.next())
                while (aux in newItinerary) {
                    aux = (aux + 1) % MAX_CITIES
                }
                newItinerary[i] = aux
            }
            return (Solution(newItinerary))
        }


    }
}