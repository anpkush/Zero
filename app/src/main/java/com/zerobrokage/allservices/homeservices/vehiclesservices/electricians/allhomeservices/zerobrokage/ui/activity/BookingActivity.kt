package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityBookingBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CustomeDoneDialogBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.BookingRequest
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                fetchCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getInt("id", 0)

        cartItems = intent.getParcelableArrayListExtra("cartItems") ?: ArrayList()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.toolbar.ivBack.setOnClickListener { finish() }
        binding.toolbar.tvTitle.text = "Booking Details"

        binding.rvDate.setOnClickListener { showDatePicker() }
        binding.rvTime.setOnClickListener { showTimePicker() }
        binding.btSubmit.setOnClickListener { validateAndSubmitBooking() }
        binding.tvMyLocation.setOnClickListener { requestLocationPermission() }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
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

        val subMenuIds = cartItems.map { it.sub_menu_id }
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
            override fun onResponse(call: Call<BookingRequest?>, response: Response<BookingRequest?>) {
                if (response.isSuccessful) showSuccessDialog() else showFailureDialog("Failed to create booking. Please try again.")
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
        val dialog = AlertDialog.Builder(this@BookingActivity).setView(dialogBinding.root).create()
        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this@BookingActivity, HomeActivity::class.java))
            finish()
        }
        dialogBinding.ivDone.setImageResource(R.drawable.done)
        dialogBinding.tvCong.text = "Congratulations"
        dialogBinding.done.text = "Your booking has been completed successfully!"
        dialog.show()
    }

    private fun showFailureDialog(errorMessage: String) {
        val dialogBinding = CustomeDoneDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this@BookingActivity).setView(dialogBinding.root).create()
        dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }
        dialogBinding.ivDone.setImageResource(R.drawable.fail)
        dialogBinding.tvCong.text = "Booking Failed"
        dialogBinding.done.text = errorMessage
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun fetchCurrentLocation() {
        if (isLocationEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) return

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        try {
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            if (!addresses.isNullOrEmpty()) {
                                val address = addresses[0]
                                val state = address.adminArea
                                val city = address.locality
                                val pincode = address.postalCode
                                val roadName = address.thoroughfare
                                val buildingName = address.featureName
                                val local = address.subLocality

                                binding.etState.setText(state)
                                binding.etCity.setText(city)
                                binding.etPincode.setText(pincode)
                                binding.etHouseNo.setText("$buildingName, $local")
                                binding.etRoadArea.setText("$local, $city, $roadName")
                            } else {
                                Toast.makeText(this, "Unable to Get Location", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this, "Failed to fetch address", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Failed to get location: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LocationManager::class.java)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            binding.rvDate.setText(String.format("%04d-%02d-%02d", year, month + 1, day))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).apply {
            datePicker.minDate = calendar.timeInMillis
            show()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            binding.rvTime.setText(String.format("%02d:%02d", hour, minute))
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }
}
