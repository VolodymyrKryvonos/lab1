package org.example.lab5

import jcuda.Pointer
import jcuda.Sizeof
import jcuda.runtime.*
import java.nio.ByteBuffer


class Matrix(val columns: Int, val rows: Int) : java.io.Serializable {
    constructor(n: Int) : this(n, n)
    constructor(byteBuffer: ByteBuffer, n:Int):this(n, n){
        repeat(n){ i->
            repeat(n){ j->
                matrix[i][j] = byteBuffer.get<Float>(i*n+j)
            }
        }
    }

    private var matrix: MutableList<MutableList<Float>> = MutableList(rows) { MutableList(columns) { 0F } }

    operator fun times(multiplayer: Matrix): Matrix {
        if (multiplayer.columns == 1 && multiplayer.rows == 1) {
            return this * multiplayer[0, 0]
        }
        if (columns == multiplayer.rows) {
            val matrix = Pointer()
            val multiplayerPointer = Pointer()
            val dimension = Pointer()
            JCuda.cudaMalloc(matrix, Sizeof.FLOAT * rows.toLong() * columns.toLong())
            JCuda.cudaMalloc(multiplayerPointer, Sizeof.FLOAT * rows.toLong() * columns.toLong())
            JCuda.cudaMalloc(dimension, Sizeof.INT.toLong())
            dimension.byteBuffer.put(rows.toByte())
            matrix.byteBuffer.put(this.matrix.toByteArray())
            multiplayerPointer.byteBuffer.put(multiplayer.matrix.toByteArray())
            JCudaKernel.executeKernel("kernelf.cu", dimension, matrix, multiplayerPointer)
            val result = Matrix(matrix.byteBuffer, rows)
            JCuda.cudaFree(dimension)
            JCuda.cudaFree(matrix)
            JCuda.cudaFree(multiplayerPointer)
            return result
        }
        throw Exception("invalid shape")
    }

    operator fun times(multiplayer: Float): Matrix {
        val result: MutableList<MutableList<Float>> = MutableList(rows) { MutableList(columns) { 0F } }
        for ((i, row) in matrix.withIndex()) {
            for ((j, _) in row.withIndex()) {
                result[i][j] = matrix[i][j] * multiplayer
            }
        }
        return Matrix(columns, rows).apply { matrix = result }
    }

    operator fun plus(value: Matrix): Matrix {
        val result: MutableList<MutableList<Float>> = MutableList(rows) { MutableList(columns) { 0F } }
        for ((i, row) in matrix.withIndex()) {
            for ((j, _) in row.withIndex()) {
                result[i][j] = matrix[i][j] + value[i, j]
            }
        }
        return Matrix(columns, rows).apply { matrix = result }
    }

    operator fun get(i: Int, j: Int): Float {
        return matrix[i][j]
    }

    operator fun set(i: Int, j: Int, value: Float) {
        matrix[i][j] = value
    }

    fun transpose(): Matrix {
        val result: MutableList<MutableList<Float>> = MutableList(columns) { MutableList(rows) { 0F } }
        repeat(columns) { i ->
            repeat(rows) { j ->
                result[i][j] = matrix[j][i]
            }
        }
        return Matrix(result.first().size, result.size).apply {
            matrix = result
        }
    }

    override fun toString(): String {
        val s = StringBuilder()
        for (row in matrix) {
            for (value in row) {
                s.append(String.format("%7s ", String.format("%.2f", value)))
            }
            s.append("\n")
        }
        return s.toString()
    }

    operator fun minus(c2: Matrix): Matrix {
        val result: MutableList<MutableList<Float>> = MutableList(rows) { MutableList(columns) { 0F } }
        for ((i, row) in matrix.withIndex()) {
            for ((j, _) in row.withIndex()) {
                result[i][j] = matrix[i][j] - c2[i, j]
            }
        }
        return Matrix(columns, rows).apply { matrix = result }
    }

    companion object {

        var i = 0
        var j = 0
        var k = 0
        var recDepth = 0
        private fun multiplyMatrixRec(A: Matrix, B: Matrix, C: Matrix) {
//            recDepth++
//            println(recDeapth)
            if (i >= A.rows)
                return

            // If i < row1
            if (j < B.columns) {
                if (k < A.columns) {
                    C[i, j] += A[i, k] * B[k, j]
                    k++

                    multiplyMatrixRec(
                        A, B, C
                    )
                }

                k = 0
                j++
                multiplyMatrixRec(A, B, C)
            }
            j = 0
            i++
            multiplyMatrixRec(A, B, C);
        }

        // Function to multiply two matrices A[][] and B[][]
        fun multiplyMatrix(A: Matrix, B: Matrix): Matrix {
            if (B.rows != A.columns) {
                throw Exception("Not Possible")
            }

            val C = Matrix(A.rows)

            multiplyMatrixRec(A, B, C)
            j = 0
            i = 0
            k = 0
            recDepth = 0
            return C
        }
    }


}

private fun <T> ByteBuffer.get(i: Int): Float{
    return 0F
}

private fun <E> MutableList<E>.toByteArray(): ByteArray {
    return ByteArray(0)
}

object JCudaKernel {
    fun executeKernel(fileName: String, vararg pointers: Pointer) {

    }
}

