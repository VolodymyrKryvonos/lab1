package org.example.lab2

class TaskFlow(var independentTasks: ArrayList<Task>) {

    fun start(){
        for (task in independentTasks){
            Thread(task.runnable).start()
        }
    }

    var result: Any? = null

    class Task(executable:()-> Unit) {

        var dependentTasksCount: Int = 0
        var childTask: ArrayList<Task> = arrayListOf()
        val runnable = Runnable {
            executable()
            childTask.forEach{
                it.removeDependence()
            }
        }

        private fun addDependence(){
            dependentTasksCount++
        }

        private fun removeDependence(){
            dependentTasksCount--
            if (dependentTasksCount==0){
                Thread(runnable).start()
            }
        }

        fun add(task: Task) {
            childTask.add(task)
            task.addDependence()
        }
    }
}