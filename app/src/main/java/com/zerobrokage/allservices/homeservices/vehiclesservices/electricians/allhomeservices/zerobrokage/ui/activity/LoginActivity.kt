package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var sharedPref: SharedPreferences
    private var isRequestInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        retrieveUserData()

        binding.btGetOtp.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val mobileNo = binding.etMobileNumber.text.toString().trim()
            val countryCode = binding.countryPeaker.selectedCountryCodeWithPlus

            if (validateInputs(name, mobileNo, countryCode)) {
                if (!isRequestInProgress) {
                    getOtp(countryCode, mobileNo, name)
                    isRequestInProgress = true
                    binding.btGetOtp.isEnabled = false
                }
            }
        }
    }

    private fun retrieveUserData() {
        val mobileNumber = sharedPref.getString("mobile_number", "")
        val name = sharedPref.getString("name", "")

        binding.etMobileNumber.setText(mobileNumber)
        binding.etName.setText(name)
    }

    private fun validateInputs(name: String, mobileNo: String, countryCode: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (mobileNo.isEmpty()) {
            Toast.makeText(this, "Please Enter Mobile Number ", Toast.LENGTH_SHORT).show()
            return false
        }
        if (mobileNo.length != 10 || !mobileNo.all { it.isDigit() } || mobileNo.all { it == '0' }) {
            Toast.makeText(this, "Please Enter Valid Number ", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun getOtp(countryCode: String, mobileNo: String, name: String) {
        val userId = sharedPref.getInt("id", 0)

        sharedPref.edit().apply {
            putString("name", name)
            putString("mobile_number", mobileNo)
            putString("country_code", countryCode)
            putInt("id", userId)
            apply()
        }

        val loginRequest = LoginRequest(countryCode, mobileNo, name)
        RetrofitInstance.apiService.postSendOtp(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Otp Sent", Toast.LENGTH_SHORT).show()

                        val bundle = Bundle().apply {
                            putString("name", name)
                            putString("mobile_number", mobileNo)
                            putString("countryCode", countryCode)
                            putInt("id", userId)
                        }
                        val intent = Intent(this@LoginActivity, OtpActivity::class.java).apply {
                            putExtras(bundle)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Failed to send OTP. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error Server Issue: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
    private fun resetButtonState() {
        isRequestInProgress = false
        binding.btGetOtp.isEnabled = true
    }
}
