package com.gimmesun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gimmesun.model.WeatherByHours
import kotlinx.coroutines.launch

class SingleDayForecastViewModel(weatherByHoursList: ArrayList<WeatherByHours>) : ViewModel() {
    private val _morningTemp = MutableLiveData<String>()
    val morningTemp: LiveData<String>
        get() = _morningTemp

    private val _dayTemp = MutableLiveData<String>()
    val dayTemp: LiveData<String>
        get() = _dayTemp

    private val _nightTemp = MutableLiveData<String>()
    val nightTemp: LiveData<String>
        get() = _nightTemp

    private val _humidity = MutableLiveData<String>()
    val humidity: LiveData<String>
        get() = _humidity

    init {
        viewModelScope.launch {
            var humidity: Int? = null
            weatherByHoursList.forEach {
                if (it.timestampTxt.contains("06:00:00")) {
                    _morningTemp.postValue(String.format("%.2f ℃", it.mainData.temperature))
                }
                if (it.timestampTxt.contains("12:00:00")) {
                    _dayTemp.postValue(String.format("%.2f ℃", it.mainData.temperature))
                    humidity = it.mainData.humidity
                }
                if (it.timestampTxt.contains("21:00:00")) {
                    _nightTemp.postValue(String.format("%.2f ℃", it.mainData.temperature))
                    if (humidity == null) humidity = it.mainData.humidity
                }
            }
            _humidity.postValue(humidity.toString())
        }
    }
}