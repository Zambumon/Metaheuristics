package carlos.almuina

import java.lang.Integer.max
import java.lang.Integer.min

fun cost(distances: Matrix, solution: Solution): Int {
    var total = 0
    var previous = 0
    solution.itinerary.forEach { i ->
        total += distances[previous, i]
        previous = i
    }
    return total + distances[previous, 0]
}

data class Solution(var itinerary: Array<Int>) {

    fun isBetter(a: Solution, distanceMatrix: Matrix): Boolean {
        return (cost(distanceMatrix, this) < cost(distanceMatrix, a))
    }


    fun toString(matrix: Matrix): String {
        var extra = ""
        repeat(itinerary.size){ extra += "${itinerary[it]} " }
        return "FUNCION OBJETIVO (km): ${cost(matrix, this)}, RECORRIDO: " + extra
    }

    fun recorrido():String{
        var extra = ""
        repeat(itinerary.size){extra += "${itinerary[it]} "}
        return extra
    }


    fun print(m: Matrix) {
        print("\tRECORRIDO: ")
        repeat(itinerary.size) {
            print("${itinerary[it]} ")
        }
        println("\n\tFUNCION OBJETIVO (km): ${cost(m, this)}")
    }

    fun swap(aPosition: Int, otherPosition : Int) : Solution {
        val start = min(aPosition, otherPosition)
        val end = max(aPosition, otherPosition)
        val init = this.itinerary.copyOfRange(0,start)
        val mid = this.itinerary.copyOfRange(start,end)
        val last = this.itinerary.copyOfRange(end, this.itinerary.size)
        return Solution((init + (mid).reversedArray() + last).reversedArray())
    }

    fun getBestNeighbour(givenPosition: Int, distanceMatrix: Matrix): Pair<Solution, Int> {
        var best = Solution(itinerary.clone())
        var bestCost = Int.MAX_VALUE
        var index = -1

        val extracted = this.itinerary.copyOfRange(0, givenPosition) + this.itinerary.copyOfRange(givenPosition + 1, this.itinerary.size)
        val city = this.itinerary[givenPosition]

        repeat(CITIES) {
            val testing = swap(givenPosition,it)

            val cost = cost(distanceMatrix, testing)
            if (cost < bestCost && givenPosition != it) {
                best = testing
                bestCost = cost
                index = it
            }
        }
        return Pair(best, index)
    }


    companion object {
        fun init(random: Random, matrix: Matrix): Solution {
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

        fun greedyInit(matrix: Matrix, starter : Int): Solution {
            val newItinerary = Array(CITIES) { 0 }
            val visited = Array(MAX_CITIES) { false }
            var previous = starter
            var city = -1
            var min: Int
            newItinerary[0] = starter
            visited[starter] = true
            repeat(CITIES -1) {
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
                newItinerary[it+1] = city
            }
            return (Solution(newItinerary))
        }
    }
}