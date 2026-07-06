package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : BaseViewModel(application) {
    private val personRepository = PersonRepository(application.applicationContext)
    private val _createUser = MutableLiveData<ValidationModel>()
    val createUser: LiveData<ValidationModel> = _createUser

    fun create(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = personRepository.create(name, email, password, "false")
                if (response.isSuccessful && response.body() != null) {
                    val person = response.body()!!

                    RetrofitClient.addHeaders(person.token, person.personKey)

                    saveUserAuth(person)
                    _createUser.value = ValidationModel()
                } else {
                    _createUser.value = parseErrorMessage(response)
                }
            } catch (e: Exception) {
                _createUser.value = handleException(e)
            }
        }

    }

}