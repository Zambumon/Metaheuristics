package usc.carlos.almuina.varela

const val MAX_CITIES = 10
const val CITIES = MAX_CITIES-1

fun bestFirst(matrix: Matrix, random: Random): Solution {
    var current = Solution.init(random)
    var best : Solution
    var prime : Solution
    var counter = 0

    do {
        println("\nSOLUCION S_$counter -> ${current.stringify(matrix)}")
        best = current
        prime = current.getBestFirstNeighbour(matrix, random)

        if (prime.betterThan(best, matrix))
            current = prime

        counter++
    } while (!prime.worseEqualThan(best, matrix))
    return current
}


fun main(args: Array<String>) {
    val distancesPath = args.getOrNull(0) ?: "./res/distances.txt"
    val randomPath = args.getOrNull(1)

    val citiesMatrix = Matrix.createMatrix(distancesPath)
    val randomNumbers = if (randomPath == null) RandomGenerator() else RandomFile.load(randomPath)

    bestFirst(citiesMatrix, randomNumbers)


}