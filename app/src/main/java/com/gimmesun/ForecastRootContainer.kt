package com.gimmesun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.gimmesun.model.FinalData
import com.gimmesun.model.WeatherPackage
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class ForecastRootContainer : Fragment() {

    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager2

    companion object {
        const val ARG_WEATHER_PACKAGE = "ARG_WEATHER_PACKAGE"
        fun newInstance(weatherPackage: WeatherPackage): ForecastRootContainer {
            val bundle = Bundle()
            bundle.putParcelable(ARG_WEATHER_PACKAGE, weatherPackage)
            val fragment = ForecastRootContainer()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forecast_root_container_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val data: WeatherPackage? = it.getParcelable(ARG_WEATHER_PACKAGE)
            val finalData = FinalData(data)

            pagerAdapter = PagerAdapter(this, finalData)
            viewPager = view.findViewById(R.id.pager)
            viewPager.adapter = pagerAdapter

            val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val timestamp = finalData.sortedMap.keys.toTypedArray()[position]
                val formatter = SimpleDateFormat("dd.MM", Locale.getDefault())
                tab.text = formatter.format(Date(timestamp))
            }.attach()
        }
    }


}