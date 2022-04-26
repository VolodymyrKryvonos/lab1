package org.example.lab1

import java.lang.Thread.sleep
import java.util.*
import kotlin.random.Random

class TaskExecutor: Runnable {

    private lateinit var currentTask: Task
    private val random = Random(Date().time)
    var executor: Thread = Thread()
    val tasks = arrayListOf<Task>()
    fun execute(task: Task) {
        if (executor.isAlive) {
            task.finishedTime = Date()
            task.state = State.SKIPPED
            tasks.add(task)
            return
        }
        currentTask = task
        executor = Thread(this)
        executor.start()
    }

    override fun run() {
        currentTask.state = State.PROCESSING
        currentTask.executingTime = random.nextLong(1, 3) * 100
        sleep(currentTask.executingTime)
        currentTask.finishedTime = Date()
        currentTask.state = State.DONE
        tasks.add(currentTask)
    }
}