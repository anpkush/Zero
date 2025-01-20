package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityBookingBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CustomeDoneDialogBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartViewApi
import java.util.*

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = 0
    private lateinit var cartItems: ArrayList<CartViewApi.Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getInt("id", 0)

       // cartItems = intent.getParcelableArrayListExtra("cartItems") ?: ArrayList()

        binding.toolbar.ivBack.setOnClickListener { finish() }
        binding.toolbar.tvTitle.text = "Booking Details"

        binding.rvDate.setOnClickListener { showDatePicker() }
        binding.rvTime.setOnClickListener { showTimePicker() }

       // binding.btSubmit.setOnClickListener { validateAndSubmitBooking() }
    }

    /*private fun validateAndSubmitBooking() {
        val bookingDate = binding.rvDate.text.toString()
        val bookingTime = binding.rvTime.text.toString()
        val fullName = binding.etFullName.text.toString()
        val email = binding.etEmailId.text.toString()
        val mobileNumber = binding.etMobileNumber.text.toString()
        val fullAddress = "${binding.etHouseNo.text} ${binding.etRoadArea.text}"

        if (bookingDate.isEmpty() || bookingTime.isEmpty() || fullName.isEmpty() || mobileNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val subMenuIds = cartItems.map { it.id }
        val totalQty = cartItems.sumOf { it.qty }

        val bookingRequest = BookingRequest(
            sub_menu_id = subMenuIds,
            booking_date = bookingDate,
            booking_time = bookingTime,
            qty = totalQty,
            full_address = fullAddress,
            name = fullName,
            email = email,
            mobile_number = mobileNumber
        )

        RetrofitInstance.apiService.createBooking(userId, bookingRequest).enqueue(object :
            Callback<BookingResponse> {
            override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    showSuccessDialog()
                } else {
                    Toast.makeText(this@BookingActivity, "Booking failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                Toast.makeText(this@BookingActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/

    private fun showSuccessDialog() {
        val dialogBinding = CustomeDoneDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()

        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        dialog.show()
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
}
