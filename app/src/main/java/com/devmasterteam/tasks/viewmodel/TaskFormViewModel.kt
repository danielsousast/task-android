package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskFormViewModel(application: Application) : BaseViewModel(application) {

    val priorityRepository = PriorityRepository(application.applicationContext)
    val taskRepository = TaskRepository(application.applicationContext)
    val priorityList = priorityRepository.getLocalList().asLiveData()

    private val _taskSaved = MutableLiveData<ValidationModel>()
    val taskSaved: LiveData<ValidationModel> = _taskSaved

    private val _taskLoaded = MutableLiveData<ValidationModel>()
    val taskLoaded: LiveData<ValidationModel> = _taskLoaded


    private val _task = MutableLiveData<TaskModel>()
    val task: LiveData<TaskModel> = _task


    fun save(task: TaskModel) {
        viewModelScope.launch {
            try {
                val response = if (task.id == 0) {
                    taskRepository.save(task)
                } else {
                    taskRepository.update(task)
                }
                if (response.isSuccessful && response.body() != null) {
                    _taskSaved.value = ValidationModel()
                } else {
                    _taskSaved.value = parseErrorMessage(response)
                }
            } catch (e: Exception) {
                _taskSaved.value = handleException(e)
            }
        }
    }

    fun load(taskId: Int) {
        viewModelScope.launch {
            try {
               val response = taskRepository.load(taskId)
                if (response.isSuccessful && response.body() != null) {
                    _task.value = response.body()!!
                } else {
                    _taskLoaded.value = parseErrorMessage(response)
                }
            } catch (e: Exception) {
                _taskLoaded.value = handleException(e)
            }
        }
    }
}