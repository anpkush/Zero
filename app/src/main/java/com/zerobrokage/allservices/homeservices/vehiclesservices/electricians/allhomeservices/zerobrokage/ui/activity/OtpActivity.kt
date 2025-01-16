package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityOtpBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.OtpRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.OtpVerificationResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.ResendOtp
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var sharedPref: SharedPreferences
    private var mobileNumber: String? = null
    private var countryCode: String? = null
    private var name: String? = null
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        mobileNumber = intent.extras?.getString("mobile_number")
        countryCode = intent.extras?.getString("countryCode")
        name = intent.extras?.getString("companyName")

        if (mobileNumber == null || countryCode == null) {
            Toast.makeText(this, "Invalid data. Please try again.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.tvChangeNumber.text = "$countryCode $mobileNumber"

        binding.ivEditNumber.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
                putExtra("mobile_number", mobileNumber)
                putExtra("name", name)
            }
            startActivity(intent)
            finish()
        }

        binding.btVerify.setOnClickListener {
            val enteredOtp = binding.pinView.text.toString().trim()
            if (enteredOtp.isNotEmpty()) {
                if (mobileNumber != null) {
                    verifyOtp(mobileNumber!!, enteredOtp)
                } else {
                    Toast.makeText(this, "Mobile number is missing", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvResend.setOnClickListener {
            Toast.makeText(this, "Resend Otp", Toast.LENGTH_SHORT).show()
            resendOtp()
        }
        startTimer()
    }

    private fun resendOtp() {
        if (mobileNumber != null && countryCode != null) {
            val resendOtp = ResendOtp(country_code = countryCode!!, mobile_number = mobileNumber!!)

            RetrofitInstance.apiService.postResendOtp(resendOtp)
                .enqueue(object : Callback<ResendOtp?> {
                    override fun onResponse(
                        call: Call<ResendOtp?>,
                        response: Response<ResendOtp?>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@OtpActivity,
                                "OTP Resent Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            startTimer()
                        } else {
                            Toast.makeText(
                                this@OtpActivity,
                                "Failed to resend OTP",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResendOtp?>, t: Throwable) {
                        Toast.makeText(this@OtpActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        } else {
            Toast.makeText(this, "Mobile number or country code is missing", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startTimer() {
        binding.tvResend.isEnabled = false
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                binding.timer.text =
                    String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60)
            }

            override fun onFinish() {
                binding.timer.text = "Done!"
                binding.tvResend.isEnabled = true
                binding.btVerify.isEnabled = true
            }
        }.start()
    }

    private fun verifyOtp(mobileNumber: String, otp: String) {
        binding.btVerify.isEnabled = false
        val otpRequest = OtpRequest(mobile_number = mobileNumber, otp = otp)

        RetrofitInstance.apiService.postOtp(otpRequest)
            .enqueue(object : Callback<OtpVerificationResponse> {
                override fun onResponse(
                    call: Call<OtpVerificationResponse>,
                    response: Response<OtpVerificationResponse>
                ) {
                    binding.btVerify.isEnabled = true

                    if (response.isSuccessful) {
                        val verificationResponse = response.body()
                        verificationResponse?.let {
                            if (it.success) {
                                sharedPref.edit().putBoolean("isLoggedIn", true).apply()

                                sharedPref.edit().apply {
                                    putString("mobile_number", mobileNumber)
                                    putString("name", name)
                                    apply()
                                }

                                Toast.makeText(this@OtpActivity, "Verified", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@OtpActivity, HomeActivity::class.java)
                                sharedPref.edit().putInt("id", it.id).apply()
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@OtpActivity, it.message, Toast.LENGTH_SHORT).show()
                            }
                            finish()
                        }
                    } else {
                        Toast.makeText(this@OtpActivity, "Verification failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OtpVerificationResponse>, t: Throwable) {
                    binding.btVerify.isEnabled = true
                    Toast.makeText(this@OtpActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
