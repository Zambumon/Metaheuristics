package usc.carlos.almuina.varela

const val MAX_CITIES = 100
const val CITIES = MAX_CITIES - 1
const val TENANCY = 100
const val ITERATIONS = 10000


fun bestFirst(matrix: Matrix, random: Random): Solution {
    var current = Solution.init(random)
    var best: Solution
    var prime: Solution
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
    val distancesPath = args.getOrNull(0) ?: "./res/Taboo/distances.txt"
    val randomPath = args.getOrNull(1)

    val citiesMatrix = Matrix.createMatrix(distancesPath)
    val randomNumbers = if (randomPath == null) RandomGenerator() else RandomFile.load(randomPath)
    val solution = Solution.init(randomNumbers)
    println("RECORRIDO INICIAL")
    println(solution.toString(citiesMatrix))
    solution.tabooSearch(citiesMatrix)

}