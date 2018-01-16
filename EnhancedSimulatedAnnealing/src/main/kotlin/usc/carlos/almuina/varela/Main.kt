package usc.carlos.almuina.varela


fun main(args: Array<String>) {
    val distancesPath = args.getOrNull(0) ?: "./res/distancias_sa_100_2017.txt"
    val randomPath = args.getOrNull(1)
    val citiesMatrix = Matrix.loadMatrix(distancesPath)
    val randomNumbers = if (randomPath == null) RandomGenerator() else RandomFile.load(randomPath)
    val solution = Solution.greedyInit(citiesMatrix)
    val simulated = SimulatedAnnealing(solution,randomNumbers,citiesMatrix,0.0,0.0,0)
    simulated.go()
}