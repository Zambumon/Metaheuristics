package usc.carlos.almuina.varela

import java.util.*

data class Solution(
        private val itinerary: Array<Int>
) {


    // Starting from city 0, check the distances between each pair of cities and add them to the total.
    // Additionally, we'll have to add the return trip to city 0.
    fun cost(citiesDistances: Matrix): Int {
        var total = 0
        var previous = 0
        require(citiesDistances.size == (MAX_CITIES))
        itinerary.forEach { i ->
            total += citiesDistances[previous, i]
            previous = i
        }
        return total + citiesDistances[previous, 0]
    }

    fun betterThan(other: Solution, matrix: Matrix): Boolean = this.cost(matrix) < other.cost(matrix)

    fun worseEqualThan(other: Solution, matrix: Matrix) = !betterThan(other, matrix)



    fun stringify(citiesDistances: Matrix) = "${Arrays.toString(itinerary)}; ${cost(citiesDistances)}km"

    private fun newNeighbour(permuteA: Int, permuteB: Int): Solution {
        val neighbour = Solution(this.itinerary.clone())
        neighbour.itinerary[permuteA] = itinerary[permuteB]
        neighbour.itinerary[permuteB] = itinerary[permuteA]
        return neighbour
    }


    fun getBestFirstNeighbour(matrix: Matrix, random: Random): Solution {
        val neighbourhoodSize = (MAX_CITIES-1) * (MAX_CITIES-2) / 2
        var prime : Solution
        var neighbourIndex = 0
        val explored = Matrix.emptyMatrix(itinerary.size)

        do {
            var (rand1, rand2) = random.nextPair()
            while (rand1==rand2 || explored[rand1, rand2] != 0) { // First condition could happen while iterating
                if (rand2 == rand1) {
                    rand1 = (rand1 + 1) % CITIES
                    rand2 = 0
                    if (rand1 == 0)
                        rand1 = 1
                } else {
                    rand2++
                }
            }

            prime = this.newNeighbour(rand1, rand2)
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
            // -1 since city 0 is discarded from the solution array.
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