package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityBookingBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CustomeDoneDialogBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.BookingRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.BookingResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.R
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = 0
    private lateinit var cartItems: ArrayList<CartData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getInt("id", 0)

        cartItems = intent.getParcelableArrayListExtra("cartItems") ?: ArrayList()

        binding.toolbar.ivBack.setOnClickListener { finish() }
        binding.toolbar.tvTitle.text = "Booking Details"

        binding.rvDate.setOnClickListener { showDatePicker() }
        binding.rvTime.setOnClickListener { showTimePicker() }

        binding.btSubmit.setOnClickListener {
            validateAndSubmitBooking()
        }
    }

    private fun validateAndSubmitBooking() {
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
        binding.btSubmit.isEnabled = false

        RetrofitInstance.apiService.createBooking(userId, bookingRequest).enqueue(object : Callback<BookingRequest?> {
            override fun onResponse(
                call: Call<BookingRequest?>,
                response: Response<BookingRequest?>
            ) {
                if (response.isSuccessful) {
                    showSuccessDialog()
                } else {
                    showFailureDialog("Failed to create booking. Please try again.")
                }
                binding.btSubmit.isEnabled = true
            }

            override fun onFailure(call: Call<BookingRequest?>, t: Throwable) {
                showFailureDialog(t.message ?: "Unknown error occurred.")
                binding.btSubmit.isEnabled = true
            }
        })
    }

    private fun showSuccessDialog() {
        val dialogBinding = CustomeDoneDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()

        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }


        dialogBinding.ivDone.setImageResource(R.drawable.done)
        dialogBinding.tvCong.text = "Congratulations"
        dialogBinding.done.text = "Your booking has been completed successfully!"

        dialog.show()
    }

    private fun showFailureDialog(errorMessage: String) {
        val dialogBinding = CustomeDoneDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()

        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.ivDone.setImageResource(R.drawable.fail)
        dialogBinding.tvCong.text = "Booking Failed"
        dialogBinding.done.text = "Your booking has been not completed"

        dialog.show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                binding.rvDate.setText(String.format("%04d-%02d-%02d", year, month + 1, day))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = today

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                binding.rvTime.setText(
                    String.format("%02d:%02d", hour, minute)
                )
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}
