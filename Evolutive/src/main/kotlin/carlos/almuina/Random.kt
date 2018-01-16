package carlos.almuina

import java.io.File

interface Random {
    fun next(): Int
    fun nextRaw() : Double
    fun nextChallenger():Int
    fun nextPair(): Pair<Int, Int>
}

fun preparePair(rand1: Double, rand2: Double): Pair<Int, Int> {
    var a = Math.floor(CITIES * rand1).toInt()
    var b = rand2.toInt()
    return Pair(a,b)
}


//Loading randoms from an specified file

data class RandomFile(private val values: MutableList<Double>) : Random {

    override fun next(): Int = Math.floor(CITIES * values.removeAt(0)).toInt()
    override fun nextRaw(): Double = values.removeAt(0)
    override fun nextChallenger() : Int = Math.floor(POPULATION * values.removeAt(0)).toInt()

    override fun nextPair(): Pair<Int, Int> = preparePair(values.removeAt(0), values.removeAt(0))

    companion object {
        fun load(pathname: String): Random {
            if (!File(pathname).exists()) throw IllegalStateException("File ${File(pathname).absolutePath} does not exists")
            return RandomFile(File(pathname).readLines().map { it.toDouble() }.toMutableList())
        }
    }
}

// Generating randoms using Math

class RandomGenerator : Random {
    override fun next(): Int = Math.floor(CITIES * Math.random()).toInt()
    override fun nextRaw(): Double = Math.random()
    override fun nextChallenger() : Int = Math.floor(POPULATION * Math.random()).toInt()

    override fun nextPair(): Pair<Int, Int> = preparePair(Math.random(), Math.random())

}