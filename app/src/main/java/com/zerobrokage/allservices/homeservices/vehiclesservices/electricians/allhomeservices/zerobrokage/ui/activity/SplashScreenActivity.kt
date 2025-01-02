package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Secure
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivitySplashScreenBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.DeviceID
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun getDeviceId(context: Context): String? {
            return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        }

        val deviceId = getDeviceId(this)
        binding.tvDeviceId.text = deviceId ?: "Device Id not Available"

        sendDeviceIdToServer(deviceId)

        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)


        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen(isLoggedIn)
        }, 2000)

    }

    private fun navigateToNextScreen(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }

    private fun sendDeviceIdToServer(deviceId: String?) {
        if (deviceId != null) {
            val apiService = RetrofitInstance.apiService
            val deviceID = DeviceID(deviceId)
            val call = apiService.deviceId(deviceID)
            call.enqueue(object : Callback<DeviceID> {
                override fun onResponse(call: Call<DeviceID>, response: Response<DeviceID>) {

                }

                override fun onFailure(call: Call<DeviceID>, t: Throwable) {
                    Toast.makeText(this@SplashScreenActivity, "", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Device ID is not available", Toast.LENGTH_SHORT).show()
        }
    }
}

