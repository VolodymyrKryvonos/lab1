package org.example.lab1

import java.util.Date
import kotlin.random.Random

fun main() {
    val executor = TaskExecutor()
    var i = 0
    val thread = Thread {
        while (true) {
            val task = prepareTask(i)
            if (task != null) {
                executor.execute(task)
                i++
            }
        }
    }
    thread.start()
    readln()
    thread.stop()
    for (task in executor.tasks){
        println(task)
    }
    println("Tasks processed ${executor.tasks.count{it.state == State.DONE }}")
    println("Tasks destroyed ${executor.tasks.count{it.state == State.SKIPPED }}")
    println("Percentage of destroyed tasks ${executor.tasks.count{it.state == State.SKIPPED }.toFloat()/executor.tasks.size*100}%")
}


fun prepareTask(number: Int): Task? {
    val thread = object : Thread() {
        var task: Task? = null
        override fun run() {
            super.run()
            sleep(Random(Date().time).nextLong(1,3) * 100)
            task = Task(id = number, state = org.example.lab1.State.PREPARED, Date())
        }
    }
    thread.start()
    try {
        thread.join()
    }catch (e: Exception){

    }
    return thread.task
}

