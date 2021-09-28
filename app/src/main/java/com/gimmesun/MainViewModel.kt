package com.gimmesun

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimmesun.model.WeatherPackage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(application: Application) : ViewModel() {

    private val _spinnerVisibility = MutableLiveData(View.VISIBLE)
    val spinnerVisibility: LiveData<Int>
        get() = _spinnerVisibility

    private val _infoTextViewVisibility = MutableLiveData(View.GONE)
    val infoTextViewVisibility: LiveData<Int>
        get() = _infoTextViewVisibility

    private val _infoToShow = MutableLiveData<String>()
    val infoToShow: LiveData<String>
        get() = _infoToShow

    private val _onPackageReceived = MutableLiveData<WeatherPackage>()
    val onPackageReceived: LiveData<WeatherPackage>
        get() = _onPackageReceived

    init {
        getForecastData(application)
    }

    private fun getForecastData(application: Application) {
        viewModelScope.launch {
            if (isInternetConnected(application)) {
                try {
                    val response = withContext(Dispatchers.IO) {
                        fetchData()
                    }
                    val weatherPackage = Gson().fromJson(response, WeatherPackage::class.java)
                    _onPackageReceived.postValue(weatherPackage)
                    Log.e("RESPONSE", weatherPackage.toString())
                } catch (exception: Exception) {
                    exception.printStackTrace()
                } finally {
                    _spinnerVisibility.value = View.GONE
                }
            } else {
                _spinnerVisibility.value = View.GONE
                _infoTextViewVisibility.value = View.VISIBLE
                _infoToShow.postValue(application.getString(R.string.no_internet_connection))
            }
        }
    }

    private fun isInternetConnected(application: Application): Boolean {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        val result = when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }
}