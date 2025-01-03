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


        saveAddress(userId)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.toolbar.tvTitle.text = "Add Service Addresses"
        binding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvMyLocation.setOnClickListener {
            getCurrentLocation()
        }

        binding.btSaveAddress.setOnClickListener {

            saveAddress(userId)
        }

        val name = intent.getStringExtra("name")
        val address = intent.getStringExtra("address")
        val type = intent.getStringExtra("type")
        val mobileNo = intent.getStringExtra("mobileNo")

        if (name != null && address != null && type != null && mobileNo != null) {
            binding.etFullName.setText(name)
            binding.etHouseNo.setText(address)
            binding.etMobileNumber.setText(mobileNo)
            binding.rdAddressType.checkedRadioButtonId
            binding.rbHome.isChecked = true
            binding.rbWork.isChecked = true
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1001
            )
            return
        }

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val location = task.result
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val geoCoder = Geocoder(this, Locale.getDefault())
                    val addresses = geoCoder.getFromLocation(latitude, longitude, 1)

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
                        binding.etRoadArea.setText("$local, $city,$roadName")
                    } else {
                        Toast.makeText(this, "Unable to get address", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Failed to get location: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveAddress(id: Int) {
        val selectedRadioButton =
            findViewById<RadioButton>(binding.rdAddressType.checkedRadioButtonId)

        if (binding.etFullName.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.etMobileNumber.text.isNullOrEmpty() || !binding.etMobileNumber.text.toString()
                .matches("\\d{10}".toRegex())
        ) {
            Toast.makeText(this, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (binding.etEmailId.text.isEmpty()) {
            Toast.makeText(this, "Enter Email Id", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.etPincode.text.isEmpty() || !binding.etPincode.text.toString()
                .matches("\\d{6}".toRegex())
        ) {
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
                override fun onResponse(
                    call: Call<AddAddressApi?>,
                    response: Response<AddAddressApi?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(
                            this@AddAddressActivity,
                            "Address added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddAddressActivity,
                            "Error saving address",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<AddAddressApi?>, t: Throwable) {
                    Toast.makeText(
                        this@AddAddressActivity,
                        "Failed to save address, please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}
