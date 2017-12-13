package usc.carlos.almuina.varela

import java.util.*


data class Solution(var itinerary: Array<Int>) {


    fun cost(distances: Matrix): Int {
        var total = 0
        var previous = 0
        itinerary.forEach { i ->
            total += distances[previous, i]
            previous = i
        }
        return total + distances[previous, 0]
    }


    fun isBetter(a: Solution, distanceMatrix: Matrix): Boolean {
        return (this.cost(distanceMatrix) < a.cost(distanceMatrix))
    }


    fun print(m: Matrix) {
        print("\tRECORRIDO: ")
        repeat(itinerary.size) {
            print("${itinerary[it]} ")
        }
        println("\n\tFUNCION OBJETIVO (km): ${cost(m)}")
    }


    fun getBestNeighbour(givenPosition: Int, distanceMatrix: Matrix): Pair<Solution, Int> {
        var best = Solution(itinerary.clone())
        var bestCost = Int.MAX_VALUE
        var index = -1
        val costOG = this.cost(distanceMatrix)
        val extracted = this.itinerary.copyOfRange(0, givenPosition) + this.itinerary.copyOfRange(givenPosition + 1, this.itinerary.size)
        val city = this.itinerary[givenPosition]

        repeat(CITIES ) {
            if(it!=CITIES) {
                val pre = extracted.copyOfRange(0, it)
                val pos = extracted.copyOfRange(it, extracted.size)
                val c = Solution(pre + city + pos)


                val cost = c.cost(distanceMatrix)
                if (cost < bestCost && givenPosition != it) {
                    best = c
                    bestCost = cost
                    index = it
                }

            }else{
                val c = Solution(extracted+city)
                val cost = c.cost(distanceMatrix)
                if(cost < bestCost && givenPosition != it){
                    best = c
                    bestCost = cost
                    index = it
                }
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

        fun greedyInit(matrix: Matrix): Solution {
            val newItinerary = Array(CITIES) { 0 }
            val visited = Array(MAX_CITIES) { false }
            var previous = 0
            var city = -1
            var min: Int
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