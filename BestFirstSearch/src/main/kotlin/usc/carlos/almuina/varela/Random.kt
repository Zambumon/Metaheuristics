package usc.carlos.almuina.varela

import java.io.File
import java.lang.Math.*

interface Random {
    fun next(): Int
    fun nextPair(): Pair<Int, Int>
}

fun getOrderedPair(rand1: Double, rand2: Double): Pair<Int, Int> {
    var a = Math.floor(9 * rand1).toInt()
    var b = Math.floor(9 * rand2).toInt()

    if (a == b) { // Jump one line, start over with the first column.
        a = (a + 1) % CITIES // check which on we can use here
        b = 0
        if (a == 0) {
            a = 1
        }
    }

    return Pair(maxOf(a, b), minOf(a, b))
}

data class RandomFile(private val values: MutableList<Double>) : Random {

    override fun next(): Int = floor(CITIES * values.removeAt(0)).toInt()

    override fun nextPair(): Pair<Int, Int> = getOrderedPair(values.removeAt(0), values.removeAt(0))

    companion object {
        //Loads the random numbers file and maps them to an array of doubles.
        fun load(pathname: String): Random {
            if (!File(pathname).exists()) throw IllegalStateException("File ${File(pathname).absolutePath} does not exists")
            return RandomFile(File(pathname).readLines().map { it.toDouble() }.toMutableList())
        }
    }
}

class RandomGenerator : Random {
    override fun next(): Int = floor(CITIES * Math.random()).toInt()

    override fun nextPair(): Pair<Int, Int> = getOrderedPair(Math.random(), Math.random())

}