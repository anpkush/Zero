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
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginResponse.observe(this) {
            binding.btGetOtp.isEnabled = true
            if (it?.success == true) {
                val intent = Intent(this, OtpActivity::class.java)
                intent.putExtra("mobileNumber", binding.etMobileNumber.text.toString())
                intent.putExtra("countryCode", binding.countryPeaker.selectedCountryCodeWithPlus)
                intent.putExtra("name", binding.etName.text.toString())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun validateInputs(name: String, number: String, countryCode: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter a mobile number.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (number.length != 10 || !number.all { it.isDigit() }) {
            Toast.makeText(this, "Please enter a valid mobile number.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (countryCode.isEmpty() || !countryCode.startsWith("+")) {
            Toast.makeText(this, "Please select a valid country code.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}