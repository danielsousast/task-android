package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    val priorityRepository = PriorityRepository(application.applicationContext)
    val taskRepository = TaskRepository()
    val priorityList = priorityRepository.getLocalList().asLiveData()

    fun save(task: TaskModel) {
        viewModelScope.launch {
            taskRepository.save(task)
        }
    }
}