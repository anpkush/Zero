package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityBookingBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CustomeDoneDialogBinding
import java.util.*

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.ivBack.setOnClickListener { finish() }
        binding.toolbar.tvTitle.text = "Booking Details"

        binding.rvDate.setOnClickListener {
            showDatePicker()
        }
        binding.rvTime.setOnClickListener {
            showTimePicker()
        }


        binding.btSubmit.setOnClickListener {
            allFieldCheck()


        }
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.rvDate.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }


    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val amPm = if (selectedHour >= 12) "PM" else "AM"
                val hourIn12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                val selectedTime =
                    String.format("%02d:%02d %s", hourIn12Format, selectedMinute, amPm)
                binding.rvTime.setText(selectedTime)
            },
            hour,
            minute,
            false
        )
        timePickerDialog.show()
    }

    private fun allFieldCheck() {

        val address = binding.etAddress.text.toString().trim()
        val date = binding.rvDate.text.toString().trim()
        val time = binding.rvTime.text.toString().trim()

        if (address.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
        } else {

            alertDialog()
            Toast.makeText(this, "Booking Done", Toast.LENGTH_SHORT).show()
        }
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(this)
        val binding = CustomeDoneDialogBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)

        val alertDialog = builder.create()
        binding.ivClose.setOnClickListener {
            alertDialog.dismiss()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        alertDialog.show()
    }

}
