package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityLoginBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.LoginRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.LoginResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btGetOtp.setOnClickListener {
            val mobileNumber = binding.etMobileNumber.text.toString()
            val countryCode = binding.countryPeaker.selectedCountryCodeWithPlus
            val name = binding.etName.text.toString()

            if (mobileNumber.isNotEmpty() && countryCode.isNotEmpty()) {
                val loginRequest = LoginRequest(mobile_number = mobileNumber, name = name, country_code = countryCode)
                sendOtpRequest(loginRequest)
            }
        }
    }

    private fun sendOtpRequest(loginRequest: LoginRequest) {
        RetrofitInstance.apiService.postSendOtp(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val otpResponse = response.body()
                    otpResponse?.let {
                        if (it.success) {
                            val intent = Intent(this@LoginActivity, OtpActivity::class.java).apply {
                                putExtra("mobile_number", it.mobile_number)
                                putExtra("countryCode", it.country_code)
                                putExtra("name", it.name)
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Failed to send OTP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
