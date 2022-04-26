package org.example.lab1

import java.util.Date

data class Task(
    val id: Int,
    var state: State,
    var finishedTime: Date,
    var executingTime:Long = 0
)

enum class State{
    PREPARED,
    PROCESSING,
    DONE,
    SKIPPED
}