package org.example.lab4.server

import org.example.lab2.*
import java.rmi.Remote
import java.rmi.RemoteException

class MatrixEquationSolverImpl : MatrixEquationSolver {

    override fun solveForN(n: Int): Matrix {
        var startTime: Long = 0
        var endTime: Long = 0
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

        val flow = TaskFlow(arrayListOf())

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
            println("countMatrixY1")
        }
        val countMatrixY2 = TaskFlow.Task {
            y2 = A1 * (b1 * 2F)
            println("countMatrixY2")
        }
        val countMatrixY3 = TaskFlow.Task {
            Thread.yield()
            Y3 = A2 * (B2 - C2)
            println("countMatrixY3")
        }

        // lvl 3
        val transposeY1 = TaskFlow.Task {
            y1t = y1.transpose()
            println("transposeY1")
        }
        val transposeY2 = TaskFlow.Task {
            y2t = y2.transpose()
            println("transposeY2")
        }
        val countY3Square = TaskFlow.Task {
            result = Y3 * Y3
            println("countY3Square")
        }

        var product = Matrix(0)
        //lvl 4
        val countProduct = TaskFlow.Task {
            product = y1 * y2t
            println("countProduct")
        }
        val countLvl4Result = TaskFlow.Task {
            result *= y1
            println("countLvl4Result")
        }

        //lvl 5
        val countLvl5Result = TaskFlow.Task {
            result += y2
            println("countLvl5Result")
        }
        //lvl 6
        val countLvl6Result = TaskFlow.Task {
            result = y1t * result
            println("countLvl6Result")
        }
        //lvl 7
        val countLvl7Result = TaskFlow.Task {
            result = Y3 * result
            println("countLvl7Result")
        }
        //lvl 8
        val countLvl8Result = TaskFlow.Task {
            result += product
            println("countLvl8Result")
        }
        //lvl 9
        val countLvl9Result = TaskFlow.Task {
            result = result.transpose()
            println("countLvl9Result")
        }
        //lvl 10
        val countLvl10Result = TaskFlow.Task {
            result = y2t * result
            println(result)
            endTime = System.currentTimeMillis()
            flow.result = result
            println("Execution time = ${endTime - startTime}")
        }

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
        flow.independentTasks = arrayListOf(
            countA,
            countA1,
            countA2,
            countB2,
            countC2,
            countB1
        )
        flow.start()
        while (flow.result==null){
            Thread.sleep(10)
        }
        return result
    }
}


interface MatrixEquationSolver : Remote {
    @Throws(RemoteException::class)
    fun solveForN(n: Int): Matrix
}