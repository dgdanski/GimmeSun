package com.gimmesun

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gimmesun.model.FinalData

class PagerAdapter(fragment: Fragment, private val finalData: FinalData) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5 //show five days

    override fun createFragment(position: Int): Fragment {
        val listOfDataForFiveDays = finalData.sortedMap.values.toList()
        val singleDayForecastData = listOfDataForFiveDays[position]
        return SingleDayForecastFragment.newInstance(singleDayForecastData)
    }
}