package com.gimmesun.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Parcelize
data class WeatherPackage(
    @Expose @SerializedName("cod") val code: Int,
    @Expose @SerializedName("message") val message: String,
    @Expose @SerializedName("cnt") val recordCount: Int,
    @Expose @SerializedName("list") val list: ArrayList<WeatherByHours>,
) : Parcelable

@Parcelize
data class WeatherByHours(
    @Expose @SerializedName("dt") val timestamp: Long,
    @Expose @SerializedName("main") val mainData: MainData,
    @Expose @SerializedName("weather") val weatherList: ArrayList<Weather>,
    //    @Expose @SerializedName("clouds") val clouds: ArrayList<Cloud>,
    @Expose @SerializedName("dt_txt") val timestampTxt: String
) : Parcelable

@Parcelize
data class MainData(
    @Expose @SerializedName("temp") val temperature: Float,
    @Expose @SerializedName("feels_like") val feelsLike: Float,
    @Expose @SerializedName("temp_min") val temperatureMin: Float,
    @Expose @SerializedName("temp_max") val temperatureMax: Float,
    @Expose @SerializedName("pressure") val pressure: Int,
    @Expose @SerializedName("sea_level") val seaLevel: Int,
    @Expose @SerializedName("grnd_level") val groundLevel: Int,
    @Expose @SerializedName("humidity") val humidity: Int,
    @Expose @SerializedName("temp_kf") val tempKf: Float
) : Parcelable

@Parcelize
data class Weather(
    @Expose @SerializedName("id") val id: Int,
    @Expose @SerializedName("main") val mainWeather: String,
    @Expose @SerializedName("description") val description: String,
    @Expose @SerializedName("icon") val icon: String
) : Parcelable

@Parcelize
data class Cloud(@Expose @SerializedName("all") val all: Int) : Parcelable

class FinalData(weatherPackage: WeatherPackage?) {
    lateinit var sortedMap: SortedMap<Long, ArrayList<WeatherByHours>>

    init {
        val hashmap = HashMap<Long, ArrayList<WeatherByHours>>()
        val mainList: ArrayList<WeatherByHours> = weatherPackage?.list!!
        mainList.sortBy { it.timestamp }
        if (mainList.size > 0) {
            weatherPackage.list.forEach {
                val timestamp: Long = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it.timestampTxt).time

                val cal: Calendar = Calendar.getInstance()
                cal.time = Date(timestamp)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                if (!hashmap.containsKey(cal.timeInMillis)) {
                    hashmap[cal.timeInMillis] = ArrayList()
                }
                hashmap[cal.timeInMillis]?.add(it)
            }
            sortedMap = hashmap.toSortedMap()
        }
    }
}


