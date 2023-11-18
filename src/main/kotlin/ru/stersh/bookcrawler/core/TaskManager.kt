package ru.stersh.bookcrawler.core

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

object TaskManager {
    private val taskScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val jobs = ConcurrentHashMap<String, Job>()

    fun addTask(task: Task) {
        cancelTask(task)
        jobs[task.name] = taskScope.launch {
            task.onInvokeJob()
        }
    }

    fun removeTask(task: Task) {
        cancelTask(task)
    }

    private fun cancelTask(task: Task) {
        val job = jobs[task.name] ?: return
        if (job.isCancelled) return
        job.cancel()
        jobs.remove(task.name)
    }

    interface Task {
        val name: String
        suspend fun onInvokeJob()
    }
}