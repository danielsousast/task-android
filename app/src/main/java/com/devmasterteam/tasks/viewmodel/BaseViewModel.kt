package com.devmasterteam.tasks.viewmodel

import androidx.lifecycle.AndroidViewModel
import android.app.Application
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.exception.NoInternetException
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.local.PreferencesManager
import com.google.gson.Gson
import retrofit2.Response

open class BaseViewModel(application: Application): AndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application.applicationContext)
    fun <T> errorMessage(response: Response<T>): ValidationModel {
        val msgJson = response.errorBody()?.string().toString()
        val msg = Gson().fromJson(msgJson, String::class.java)
        return ValidationModel(msg)
    }

    suspend fun saveUserAuth(person: PersonModel) {
        preferencesManager.store(TaskConstants.SHARED.TOKEN_KEY, person.token)
        preferencesManager.store(TaskConstants.SHARED.PERSON_KEY, person.personKey)
        preferencesManager.store(TaskConstants.SHARED.PERSON_NAME, person.name)
    }

    fun handleException(e: Exception): ValidationModel {
        if (e is NoInternetException) {
            return ValidationModel(e.errorMessage)
        } else {
            return ValidationModel(getApplication<Application>().applicationContext.getString(R.string.error_unexpected))
        }
    }
}