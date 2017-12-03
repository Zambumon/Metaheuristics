package usc.carlos.almuina.varela

const val MAX_CITIES = 100
const val CITIES = MAX_CITIES - 1
const val TENANCY = 1000
const val NO_IMPROVEMENT_TRESHOLD = 150
const val ITERATIONS = 10000
const val TABOO_CLEAR = 15

fun main(args: Array<String>) {
    val distancesPath = args.getOrNull(0) ?: "./res/Taboo/distances.txt"
    val citiesMatrix = Matrix.createMatrix(distancesPath)
    val solution = Solution.greedyInit(citiesMatrix)
    println("RECORRIDO INICIAL")
    println(solution.toString(citiesMatrix))
    solution.tabooSearch(citiesMatrix)
}