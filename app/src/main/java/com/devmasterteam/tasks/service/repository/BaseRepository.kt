package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.exception.NoInternetException
import retrofit2.Response

open class BaseRepository(val context: Context ) {
    suspend fun <T> safeCall(call: suspend () -> Response<T>): Response<T> {
        if (!isConnectionAvailable()) {
            throw NoInternetException(context.getString(R.string.error_internet_connection))
        }

        return call()
    }

    fun isConnectionAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNet = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNet) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}