package org.example.lab5

import kotlinx.coroutines.*
import org.example.lab2.Matrix
import org.example.lab2.square
import java.util.*
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class Equation {

    private lateinit var A: Matrix
    private lateinit var A1: Matrix
    private lateinit var A2: Matrix
    private lateinit var B2: Matrix
    private lateinit var C2: Matrix
    private lateinit var b1: Matrix
    private lateinit var y1: Matrix
    private lateinit var y2: Matrix
    private lateinit var Y3: Matrix
    private lateinit var y1t: Matrix
    private lateinit var y2t: Matrix
    private lateinit var result: Matrix
    private lateinit var product: Matrix

    private fun solve(n: Int, print:Boolean=false): Matrix {
        A = Matrix(n)
        A1 = Matrix(n)
        A2 = Matrix(n)
        B2 = Matrix(n)
        C2 = Matrix(n)
        b1 = Matrix(1, n)

        runBlocking {
            withContext(Dispatchers.IO) {
                val generateA = async {
                    println("generateA")
                    generateMatrix(A)
                }
                val generateA1 = async {
                    println("generateA1")
                    generateMatrix(A1)
                }
                val generateA2 = async {
                    println("generateA2")
                    generateMatrix(A2)
                }
                val generateB2 = async {
                    println("generateB2")
                    generateMatrix(B2)
                }
                val countB1 = async {
                    println("countB1")
                    countMatrixB()
                }
                val countC2 = async {
                    println("countC2")
                    countMatrixC()
                }
                val countY1 = async {
                    println("countY1")
                    generateA.await()
                    countB1.await()
                    countMatrixY1()
                }
                val countY2 = async {
                    println("countY2")
                    generateA1.await()
                    countB1.await()
                    countMatrixY2()
                }
                val countY3 = async {
                    println("countY3")
                    generateA2.await()
                    generateB2.await()
                    countC2.await()
                    countMatrixY3()
                }
                val transposeY1 = async {
                    println("transposeY1")
                    countY1.await()
                    transposeY1()
                }
                val transposeY2 = async {
                    println("transposeY2")
                    countY2.await()
                    transposeY2()
                }
                val countY3Square = async {
                    println("countY3Square")
                    countY3.await()
                    countY3Square()
                }
                val countProduct = async {
                    println("countProduct")
                    countY1.await()
                    transposeY2.await()
                    countProduct()
                }
                val countLvl4Result = async {
                    println("countLvl4Result")
                    countY3Square.await()
                    countY1.await()
                    countLvl4Result()
                }
                val countLvl5Result = async {
                    println("countLvl5Result")
                    countLvl4Result.await()
                    countY2.await()
                    countLvl5Result()
                }
                val countLvl6Result = async {
                    println("countLvl6Result")
                    transposeY1.await()
                    countLvl5Result.await()
                    countLvl6Result()
                }
                val countLvl7Result = async {
                    println("countLvl7Result")
                    countLvl6Result.await()
                    countLvl7Result()
                }
                val countLvl8Result = async {
                    println("countLvl8Result")
                    countProduct.await()
                    countLvl7Result.await()
                    countLvl8Result()
                }
                val countLvl9Result = async {
                    println("countLvl9Result")
                    countLvl8Result.await()
                    countLvl9Result()
                }
                val countLvl10Result = async {
                    println("countLvl10Result")
                    val t = measureTimeMillis {
                        countLvl9Result.await()
                        countLvl10Result()
                    }
                    println("Execution time = $t")
                }
                countLvl10Result.start()
            }
        }
        if (print){
            println()
        }

        return result
    }

    private fun countLvl5Result() {
        result += y2
        println("countLvl5Result finished")
    }

    private fun countLvl6Result() {
        result = y1t * result
        println("countLvl6Result finished")
    }

    private fun countLvl7Result() {
        result = Y3 * result
        println("countLvl7Result finished")
    }

    private fun countLvl8Result() {
        result += product
        println("countLvl8Result finished")
    }

    private fun countLvl9Result() {
        result = result.transpose()
        println("countLvl9Result finished")
    }

    private fun countLvl10Result() {
        result = y2t * result
        println(result)
        println("countLvl10Result finished")
    }


    private fun countProduct() {
        product = y1 * y2t
        println("countProduct finished")
    }

    private fun countLvl4Result() {
        result *= y1
        println("countLvl4Result finished")
    }

    private fun transposeY1() {
        y1t = y1.transpose()
        println("transposeY1 finished")
    }

    private fun transposeY2() {
        y2t = y2.transpose()
        println("transposeY2 finished")
    }

    private fun countY3Square() {
        result = Y3 * Y3
        println("countY3Square finished")
    }

    private fun countMatrixY1() {
        y1 = A * b1
        println("countMatrixY1 finished")
    }

    private fun countMatrixY2() {
        y2 = A1 * (b1 * 2F)
        println("countMatrixY2 finished")
    }

    private fun countMatrixY3() {
        Y3 = A2 * (B2 - C2)
        println("countMatrixY3 finished")
    }

    private fun countMatrixC() {
        repeat(C2.rows) { i ->
            repeat(C2.columns) { j ->
                C2[i, j] = 1F / (i + 1 + square(j + 1))
            }
        }
        println("countMatrixC finished")
    }

    private fun countMatrixB() {
        repeat(b1.rows) {
            b1[it, 0] = if (it % 2 == 0) {
                1f / (it - 1)
            } else {
                it.toFloat() / (it * sqrt(it.toFloat()))
            }
        }
        println("countMatrixB finished")
    }

    private fun generateMatrix(matrix: Matrix) {
        val random = Random(Date().time)
        repeat(matrix.rows) { i ->
            repeat(matrix.columns) { j ->
                matrix[i, j] = random.nextFloat()
            }
        }
    }

    fun solve(n:Int): Matrix{
        org.example.lab5.Matrix
        return solve(n/2,false)
    }

}