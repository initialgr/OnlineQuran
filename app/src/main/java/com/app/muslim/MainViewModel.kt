package com.app.muslim

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.muslim.data.Repository
import com.app.muslim.models.Data
import com.app.muslim.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    var quranResponse: MutableLiveData<NetworkResult<Data>> = MutableLiveData()

    fun getQuran(queries: Map<String, String>) = viewModelScope.launch {
        getQuranSafeCall(queries)
    }

    private suspend fun getQuranSafeCall(queries: Map<String, String>) {
        quranResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getSurat(queries)
                quranResponse.value = handleQuranResponse(response)
            } catch (e: Exception){
                quranResponse.value = NetworkResult.Error("Tidak bisa ditampilkan.")
            }
        } else {
            quranResponse.value = NetworkResult.Error("Tidak ada jaringan internet.")
        }
    }

    private fun handleQuranResponse(response: Response<Data>): NetworkResult<Data>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Waktu Habis!")
            }
            response.isSuccessful -> {
                val quranSurat = response.body()
                return NetworkResult.Success(quranSurat!!)
            }
            else -> {
             return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}