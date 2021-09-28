package com.gimmesun

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.gimmesun.databinding.ActivityMainBinding
import com.gimmesun.model.WeatherPackage
import com.gimmesun.util.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private var viewModel: MainViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        viewModel = ViewModelProvider(this, MainViewModelFactory(application)).get(MainViewModel::class.java)
        binding.apply {
            lifecycleOwner = this@MainActivity
            mainViewModel = viewModel
        }

        viewModel?.onPackageReceived?.observe(this) { weatherPackage ->
            showWeatherForecast(weatherPackage)
        }
    }

    private fun showWeatherForecast(weatherPackage: WeatherPackage) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(R.id.AppContent, ForecastRootContainer.newInstance(weatherPackage), "TAG")
            .commit()
    }
}