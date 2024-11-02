package com.example.bottomnavigationapp.weather

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bottomnavigationapp.R
import com.example.bottomnavigationapp.databinding.FragmentWeatherBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.acceptBT.setOnClickListener{
            val city =  binding.cityET.text.toString()
            getCurrentWeather(city)
        }

    }


    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    private fun getCurrentWeather(city: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getCurrentWeather(
                    city,
                    "metric",
                    requireContext().getString(R.string.api_key)
                )
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "app error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "http error ${e.message}", Toast.LENGTH_LONG)
                    .show()
                return@launch
            }

            withContext(Dispatchers.Main) {
                val data = response.body()
                binding.tempTV.text = "${data?.main?.temp}°C"
                val iconId = data?.weather?.get(0)?.icon
                val imageUrl = "https://openweathermap.org/img/wn/$iconId@4x.png"
                Picasso.get().load(imageUrl).into(binding.imageView)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}