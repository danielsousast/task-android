package com.devmasterteam.tasks.service.model

class ValidationModel(message: String = "") {

    private var success: Boolean = true
    private var errorMessage: String = ""

    init {
        if (message != "") {
            success = false
            errorMessage = message
        }
    }

    fun success(): Boolean {
        return success
    }

    fun message(): String {
        return errorMessage
    }
}