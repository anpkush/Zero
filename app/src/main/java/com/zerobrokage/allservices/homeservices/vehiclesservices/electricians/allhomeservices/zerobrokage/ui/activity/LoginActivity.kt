package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityLoginBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btGetOtp.setOnClickListener {
            val number = binding.etMobileNumber.text.toString()
            val countryCode = binding.countryPeaker.selectedCountryCodeWithPlus
            val name = binding.etName.text.toString()

            if (validateInputs(name, number, countryCode)) {
              //  binding.btGetOtp.isEnabled = false
                viewModel.login(name, countryCode, number)
            }
        }

        viewModel.loginResponse.observe(this) { response ->
        //    runOnUiThread { binding.btGetOtp.isEnabled = true }

            if (response?.success == true) {
                val intent = Intent(this, OtpActivity::class.java).apply {
                    putExtra("mobileNumber", binding.etMobileNumber.text.toString())
                    putExtra("countryCode", binding.countryPeaker.selectedCountryCodeWithPlus)
                    putExtra("name", binding.etName.text.toString())
                }
                startActivity(intent)

            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(name: String, number: String, countryCode: String): Boolean {
        return when {
            name.isEmpty() -> {
                Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show()
                false
            }
            number.isEmpty() -> {
                Toast.makeText(this, "Please enter a mobile number.", Toast.LENGTH_SHORT).show()
                false
            }
            number.length != 10 || !number.all { it.isDigit() } -> {
                Toast.makeText(this, "Please enter a valid mobile number.", Toast.LENGTH_SHORT).show()
                false
            }
            countryCode.isEmpty() || !countryCode.startsWith("+") -> {
                Toast.makeText(this, "Please select a valid country code.", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
}
