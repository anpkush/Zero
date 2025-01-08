package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityAddAddressBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.AddAddressApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AddAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAddressBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val userId = sharedPreferences.getInt("id", 0)
        val userName = sharedPreferences.getString("name", "") ?: ""
        val userMobile = sharedPreferences.getString("mobile", "") ?: ""
        val userEmail = sharedPreferences.getString("email", "") ?: ""

        binding.etFullName.setText(userName)
        binding.etMobileNumber.setText(userMobile)
        binding.etEmailId.setText(userEmail)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.toolbar.tvTitle.text = "Add Service Address"
        binding.toolbar.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.tvMyLocation.setOnClickListener { getCurrentLocation() }

        binding.btSaveAddress.setOnClickListener { saveAddress(userId) }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1001
            )
            return
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    if (location != null) {
                        val geoCoder = Geocoder(this, Locale.getDefault())
                        val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses != null && addresses.isNotEmpty()) {
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
                        }
                    }
                }
            }
    }

    private fun saveAddress(id: Int) {
        val selectedRadioButton = findViewById<RadioButton>(binding.rdAddressType.checkedRadioButtonId)

        if (binding.etFullName.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.etMobileNumber.text.isNullOrEmpty() || !binding.etMobileNumber.text.toString().matches("\\d{10}".toRegex())) {
            Toast.makeText(this, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.etEmailId.text.isEmpty()) {
            Toast.makeText(this, "Enter Email Id", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.etPincode.text.isEmpty() || !binding.etPincode.text.toString().matches("\\d{6}".toRegex())) {
            Toast.makeText(this, "Please enter a valid 6-digit Pin Code", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.etState.text.isEmpty()) {
            Toast.makeText(this, "Enter State", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.etCity.text.isEmpty()) {
            Toast.makeText(this, "Enter City", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.etHouseNo.text.isEmpty()) {
            Toast.makeText(this, "Enter House No or Building Name", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.etRoadArea.text.isEmpty()) {
            Toast.makeText(this, "Enter Colony Name or Road Name", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.rdAddressType.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please Select Address Type", Toast.LENGTH_SHORT).show()
            return
        }

        val addAddressApi = AddAddressApi(
            name = binding.etFullName.text.toString(),
            mobile_number = binding.etMobileNumber.text.toString(),
            email = binding.etEmailId.text.toString(),
            pincode = binding.etPincode.text.toString(),
            state = binding.etState.text.toString(),
            city = binding.etCity.text.toString(),
            house_number = binding.etHouseNo.text.toString(),
            road_name = binding.etRoadArea.text.toString(),
            type = selectedRadioButton.text.toString()
        )

        addAddressApi(id, addAddressApi)
    }

    private fun addAddressApi(id: Int, addAddressApi: AddAddressApi) {
        RetrofitInstance.apiService.addAddress(id, addAddressApi).enqueue(object : Callback<AddAddressApi?> {
            override fun onResponse(call: Call<AddAddressApi?>, response: Response<AddAddressApi?>) {
                if (response.isSuccessful && response.body() != null) {
                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("saved_address_name", addAddressApi.name)
                    editor.putString("saved_address_mobile", addAddressApi.mobile_number)
                    editor.putString("saved_address_email", addAddressApi.email)
                    editor.putString("saved_address_pincode", addAddressApi.pincode)
                    editor.putString("saved_address_state", addAddressApi.state)
                    editor.putString("saved_address_city", addAddressApi.city)
                    editor.putString("saved_address_house_number", addAddressApi.house_number)
                    editor.putString("saved_address_road_name", addAddressApi.road_name)
                    editor.putString("saved_address_type", addAddressApi.type)
                    editor.apply()

                    Toast.makeText(this@AddAddressActivity, "Address added successfully and saved locally", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddAddressActivity, "Error saving address", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddAddressApi?>, t: Throwable) {
                Toast.makeText(this@AddAddressActivity, "Failed to save address, please try again later", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
