package org.example.lab3

import org.example.lab2.Matrix
import java.util.*
import kotlin.random.Random
import kotlin.system.measureNanoTime

fun main(){
    val A: Matrix = generateMatrixA(10)
    val B: Matrix = generateMatrixB(10)
    println(A)
    println(B)
    calculateC(A,B)
    calculateCRec(A,B)
}
fun calculateCRec(A: Matrix, B: Matrix) {
    var C: Matrix
    val time = measureNanoTime {
        C = Matrix.multiplyMatrix(A,B)
    }
    println("Рекурсивний алгоритм")
    println(C)
    println("Час виконання $time")
}

fun generateMatrixB(n: Int): Matrix {
    val matrix = Matrix(n)
    repeat(n){
        matrix[it,it]=((it+1)*(it+2)).toFloat()
    }
    return matrix
}


fun calculateC(A: Matrix, B: Matrix){
    var C: Matrix
    val time = measureNanoTime {
        C = A*B
    }
    println("Одноразове присвоєння")
    println(C)
    println("Час виконання $time")
}


fun generateMatrixA(n: Int): Matrix {
    val rand = Random(Date().time)
    val matrix = Matrix(n)
    for (i in 0..n/2){
        for (j in i until n-i){
            matrix[i,j]=rand.nextInt(1,10).toFloat()
        }
    }
    return matrix
}
