package org.example.lab2

import java.io.File
import java.util.*
import kotlin.math.sqrt
import kotlin.random.Random

fun main() {
    val scanner = Scanner(System.`in`)
    println("Pres 1 to synchronized execution or 2 to async")
    val syncExecution = scanner.nextInt() == 1
    println("Pres 1 to print intermediate values")
    execute(
        async = !syncExecution,
        printIntermediate = scanner.nextInt() == 1
    )
}

fun execute(
    async: Boolean = true,
    printIntermediate: Boolean = false,
    randomGeneration: Boolean = true,
    manualInput: Boolean = false,
    fileInput: Boolean = false
) {
    var startTime: Long = 0
    var endTime: Long = 0
    val n = 1000
    val A = Matrix(n)
    val A1 = Matrix(n)
    val A2 = Matrix(n)
    val B2 = Matrix(n)
    val C2 = Matrix(n)
    val b1 = Matrix(1, n)
    var y1 = Matrix(0)
    var y2 = Matrix(0)
    var Y3 = Matrix(0)
    var y1t = Matrix(0)
    var y2t = Matrix(0)
    var result = Matrix(0)

    //lvl 1
    val countA = TaskFlow.Task {
        startTime = System.currentTimeMillis()
        generateMatrix(A)()
    }
    val countA1 = TaskFlow.Task(generateMatrix(A1))
    val countA2 = TaskFlow.Task(generateMatrix(A2))
    val countB2 = TaskFlow.Task(generateMatrix(B2))
    val countC2 = TaskFlow.Task(countMatrixC(C2))
    val countB1 = TaskFlow.Task(countMatrixB(b1))

    //lvl 2
    val countMatrixY1 = TaskFlow.Task {
        y1 = A * b1
        if (printIntermediate) {
            File("countMatrixY1").printWriter().use { out -> out.println(y1) }
        }
        println("countMatrixY1")
    }
    val countMatrixY2 = TaskFlow.Task {
        y2 = A1 * (b1 * 2F)
        if (printIntermediate) {
            File("countMatrixY2").printWriter().use { out -> out.println(y2) }
        }
        println("countMatrixY2")
    }
    val countMatrixY3 = TaskFlow.Task {
        Thread.yield()
        Y3 = A2 * (B2 - C2)
        if (printIntermediate) {
            File("countMatrixY3").printWriter().use { out -> out.println(Y3) }
        }
        println("countMatrixY3")
    }

    // lvl 3
    val transposeY1 = TaskFlow.Task {
        y1t = y1.transpose()
        if (printIntermediate) {
            File("transposeY1").printWriter().use { out -> out.println(y1t) }
        }
        println("transposeY1")
    }
    val transposeY2 = TaskFlow.Task {
        y2t = y2.transpose()
        if (printIntermediate) {
            File("transposeY2").printWriter().use { out -> out.println(y2t) }
        }
        println("transposeY2")
    }
    val countY3Square = TaskFlow.Task {
        result = Y3 * Y3
        if (printIntermediate) {
            File("countY3Square").printWriter().use { out -> out.println(result) }
        }
        println("countY3Square")
    }

    var product = Matrix(0)
    //lvl 4
    val countProduct = TaskFlow.Task {
        product = y1 * y2t
        if (printIntermediate) {
            File("countProduct").printWriter().use { out -> out.println(product) }
        }
        println("countProduct")
    }
    val countLvl4Result = TaskFlow.Task {
        result *= y1
        if (printIntermediate) {
            File("countLvl4Result").printWriter().use { out -> out.println(result) }
        }
        println("countLvl4Result")
    }

    //lvl 5
    val countLvl5Result = TaskFlow.Task {
        result += y2
        if (printIntermediate) {
            File("countLvl5Result").printWriter().use { out -> out.println(result) }
        }
        println("countLvl5Result")
    }
    //lvl 6
    val countLvl6Result = TaskFlow.Task {
        result = y1t * result
        if (printIntermediate) {
            File("countLvl6Result").printWriter().use { out -> out.println(result) }
        }
        println("countLvl6Result")
    }
    //lvl 7
    val countLvl7Result = TaskFlow.Task {
        result = Y3 * result
        if (printIntermediate) {
            File("countLvl7Result").printWriter().use { out -> out.println(result) }
        }
        println("countLvl7Result")
    }
    //lvl 8
    val countLvl8Result = TaskFlow.Task {
        result += product
        if (printIntermediate) {
            File("countLvl8Result").printWriter().use { out -> out.println(result) }
        }
        println("countLvl8Result")
    }
    //lvl 9
    val countLvl9Result = TaskFlow.Task {
        result = result.transpose()
        if (printIntermediate) {
            File("countLvl9Result").printWriter().use { out -> out.println(result) }
        }
        println("countLvl9Result")
    }
    //lvl 10
    val countLvl10Result = TaskFlow.Task {
        result = y2t * result
        println(result)
        endTime = System.currentTimeMillis()
        if (printIntermediate) {
            File("Result").printWriter().use { out ->
                out.println(result)
                out.println("Execution time = ${endTime - startTime}")
            }
        }
        println("Execution time = ${endTime - startTime}")
    }

    if (async) {
        countA.add(countMatrixY1)
        countB1.add(countMatrixY1)
        countB1.add(countMatrixY2)
        countA1.add(countMatrixY2)
        countA2.add(countMatrixY3)
        countC2.add(countMatrixY3)
        countMatrixY1.add(transposeY1)
        countMatrixY2.add(transposeY2)
        countMatrixY3.add(countY3Square)
        countY3Square.add(countLvl4Result)
        countMatrixY1.add(countLvl4Result)
        countMatrixY1.add(countProduct)
        transposeY2.add(countProduct)
        countMatrixY2.add(countLvl5Result)
        countLvl4Result.add(countLvl5Result)
        countLvl5Result.add(countLvl6Result)
        transposeY1.add(countLvl6Result)
        countLvl6Result.add(countLvl7Result)
        countLvl7Result.add(countLvl8Result)
        countProduct.add(countLvl8Result)
        countLvl8Result.add(countLvl9Result)
        countLvl9Result.add(countLvl10Result)
        TaskFlow(
            arrayListOf(
                countA,
                countA1,
                countA2,
                countB2,
                countC2,
                countB1
            )
        ).start()
    } else {
        executeSynchronize(
            listOf(
                countA,
                countA1,
                countA2,
                countB2,
                countC2,
                countB1,
                countMatrixY1,
                countMatrixY2,
                countMatrixY3,
                transposeY1,
                transposeY2,
                countY3Square,
                countProduct,
                countLvl4Result,
                countLvl5Result,
                countLvl6Result,
                countLvl7Result,
                countLvl8Result,
                countLvl9Result,
                countLvl10Result
            )
        )
    }
}


fun executeSynchronize(tasks: List<TaskFlow.Task>) {
    for (task in tasks) {
        task.runnable.run()
    }
}

fun generateMatrix(matrix: Matrix): () -> Unit = {
    val random = Random(Date().time)
    repeat(matrix.rows) { i ->
        repeat(matrix.columns) { j ->
            matrix[i, j] = random.nextFloat()
        }
    }
}


fun countMatrixB(matrix: Matrix): () -> Unit = {
    repeat(matrix.rows) {
        matrix[it, 0] = if (it % 2 == 0) {
            1f / (it - 1)
        } else {
            it.toFloat() / (it * sqrt(it.toFloat()))
        }
    }
}

fun countMatrixC(matrix: Matrix): () -> Unit = {
    repeat(matrix.rows) { i ->
        repeat(matrix.columns) { j ->
            matrix[i, j] = 1F / (i + 1 + square(j + 1))
        }
    }
}

fun square(value: Int) = value * value

@Synchronized
fun inputMatrix(matrix: Matrix) {
    val input = Scanner(System.`in`)
    repeat(matrix.rows) { i ->
        repeat(matrix.columns) { j ->
            matrix[i, j] = input.nextFloat()
        }
    }
}
//160510