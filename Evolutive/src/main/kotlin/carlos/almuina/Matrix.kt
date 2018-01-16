package carlos.almuina

import java.io.File
import java.util.*

data class Matrix(
        private val size: Int,
        private val distances: Array<Array<Int>>
) {

    operator fun get(x: Int, y: Int): Int {
        require(x in 0..(size - 1))
        require(y in 0..(size - 1))
        return distances[maxOf(x, y)][minOf(x, y)]
    }

    operator fun set(x: Int, y: Int, value: Int) {
        require(x in 0..(size - 1))
        require(y in 0..(size - 1))
        distances[maxOf(x, y)][minOf(x, y)] = value
    }

    fun getLine(x: Int): Array<Int> {
        require(x in 0..(size - 1))
        return distances[x]
    }

    fun print() {
        distances.forEach {
            it.forEach {
                print("%4d ".format(it))
            }
            println()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix

        if (size != other.size) return false
        if (!Arrays.equals(distances, other.distances)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size
        result = 31 * result + Arrays.hashCode(distances)
        return result
    }

    companion object {
        fun loadMatrix(pathname: String): Matrix {
            val lines = File(pathname).readLines()
            val aux = Array(lines.lastIndex + 2) { Array(lines.lastIndex + 2) { 0 } }
            lines.forEachIndexed { y, row ->
                row.split('\t').forEachIndexed { x, value ->
                    aux[y + 1][x] = value.toInt()
                }
            }

            return Matrix(lines.lastIndex + 2, aux)
        }

        fun emptyMatrix(size: Int): Matrix = Matrix(size, Array(size) { Array(size) { 0 } })
    }

}