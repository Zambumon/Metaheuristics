package carlos.almuina
fun main(args: Array<String>) {
    val distancesPath = args.getOrNull(0) ?: "./res/distancias_ce_10_2016.txt"
    val randomPath = args.getOrNull(1)
    val citiesMatrix = Matrix.loadMatrix(distancesPath)
    val randomNumbers = if (randomPath == null) RandomGenerator() else RandomFile.load(randomPath)
    val evolutive = Evolutive.init(randomNumbers, citiesMatrix)
    evolutive.go()
}