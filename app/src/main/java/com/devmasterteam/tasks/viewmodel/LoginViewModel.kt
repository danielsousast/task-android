package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.local.PreferencesManager
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val personRepository = PersonRepository()
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val preferencesManager = PreferencesManager(application.applicationContext)
    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _isUserLogged = MutableLiveData<Boolean>()
    val isUserLogged: LiveData<Boolean> = _isUserLogged

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = personRepository.login(email, password)
                if (response.isSuccessful && response.body() != null) {
                    val person = response.body()!!

                    RetrofitClient.addHeaders(person.token, person.personKey)

                    saveUserAuth(person)
                    _login.value = ValidationModel()
                } else {
                    _login.value = errorMessage(response)
                }
            } catch (e: Exception) {
                _login.value = ValidationModel(e.toString())
                println("ERROR LOG $e")
            }
        }
    }

    fun verifyLoggedUser() {
        viewModelScope.launch {
            val token = preferencesManager.get(TaskConstants.SHARED.TOKEN_KEY)
            val personKey = preferencesManager.get(TaskConstants.SHARED.PERSON_KEY)

            RetrofitClient.addHeaders(token, personKey)

            _isUserLogged.value = (token != "" && personKey != "")
        }
    }

    fun getPriorities() {
        viewModelScope.launch {
            val response =  priorityRepository.getList()

            if (response.isSuccessful && response.body() != null) {
                priorityRepository.save(response.body()!!)
            }
        }
    }
}