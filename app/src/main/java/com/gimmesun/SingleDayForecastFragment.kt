package com.gimmesun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gimmesun.databinding.SingleDayForecastFragmentBinding
import com.gimmesun.model.WeatherByHours
import com.gimmesun.util.SingleForecastViewModelFactory

class SingleDayForecastFragment : Fragment() {

    companion object {
        const val ARG_WEATHER_LIST = "ARG_WEATHER_LIST"
        fun newInstance(arrayList: ArrayList<WeatherByHours>?): SingleDayForecastFragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_WEATHER_LIST, arrayList)
            val fragment = SingleDayForecastFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: SingleDayForecastViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SingleDayForecastFragmentBinding.inflate(inflater, container, false)

        arguments?.let {
            val list = it.getParcelableArrayList<WeatherByHours>(ARG_WEATHER_LIST)
            if (list != null) {
                val viewModelFactory = SingleForecastViewModelFactory(list)
                viewModel = ViewModelProvider(this, viewModelFactory).get(SingleDayForecastViewModel::class.java)
            }
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            singleDayViewModel = viewModel
        }

        return binding.root
    }
}