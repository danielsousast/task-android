package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Response

class TaskRepository(context: Context): BaseRepository(context) {
    private val remote = RetrofitClient.getService(TaskService::class.java)

    suspend fun save(task: TaskModel): Response<Boolean> {
        return safeCall {  remote.create(
            task.priorityId,
            task.description,
            task.dueDate,
            task.complete
        )}
    }

    suspend fun list(): Response<List<TaskModel>> {
        return safeCall { remote.list() }
    }

    suspend fun listSevenDays(): Response<List<TaskModel>> {
        return safeCall { remote.listSevenDays() }
    }

    suspend fun listOverdue(): Response<List<TaskModel>> {
        return safeCall { remote.listOverdue() }
    }

    suspend fun complete(id: Int): Response<Boolean> {
        return safeCall { remote.complete(id) }
    }

    suspend fun undo(id: Int): Response<Boolean> {
        return safeCall { remote.undo(id) }
    }
}