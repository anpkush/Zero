package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityBookingBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CustomeDoneDialogBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.BookingRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("userId", 0)

        binding.toolbar.ivBack.setOnClickListener { finish() }
        binding.toolbar.tvTitle.text = "Booking Details"

        binding.rvDate.setOnClickListener { showDatePicker() }
        binding.rvTime.setOnClickListener { showTimePicker() }

        binding.btSubmit.setOnClickListener { validateAndSubmitBooking() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                binding.rvDate.setText(String.format("%02d/%02d/%d", day, month + 1, year))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                val amPm = if (hour >= 12) "PM" else "AM"
                binding.rvTime.setText(
                    String.format("%02d:%02d %s", if (hour % 12 == 0) 12 else hour % 12, minute, amPm)
                )
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun validateAndSubmitBooking() {
        val bookingRequest = BookingRequest(
            sub_menu_id = 12, // Replace with actual sub_menu_id
            booking_date = binding.rvDate.text.toString(),
            booking_time = binding.rvTime.text.toString(),
            qty = 3, // Replace with actual qty
            full_address = "${binding.etHouseNo.text} ${binding.etRoadArea.text}",
            name = binding.etFullName.text.toString(),
            email = binding.etEmailId.text.toString(),
            mobile_number = binding.etMobileNumber.text.toString()
        )

        RetrofitInstance.apiService.createBooking(userId, bookingRequest).enqueue(object :
            Callback<BookingResponse> {
            override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    showSuccessDialog()
                } else {
                    Toast.makeText(this@BookingActivity, "Booking failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                Toast.makeText(this@BookingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogBinding = CustomeDoneDialogBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        val dialog = builder.create()

        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        dialog.show()
    }
}
