package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityUpdateAddressBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.EditAddresses
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class UpdateAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateAddressBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.toolbar.tvTitle.text = "Update Address"
        binding.toolbar.ivBack.setOnClickListener { onBackPressed() }

        val userId = sharedPref.getInt("id", 0)
        val storedName = sharedPref.getString("name", "")
        val storedMobile = sharedPref.getString("mobile", "")
        val storedEmail = sharedPref.getString("email", "")
        val storedAddressType = sharedPref.getString("addressType", "")

        val intentName = intent.getStringExtra("name") ?: storedName
        val intentMobile = intent.getStringExtra("mobileNo") ?: storedMobile
        val intentEmail = intent.getStringExtra("email") ?: storedEmail
        val intentAddressType = intent.getStringExtra("addressType") ?: storedAddressType

        binding.etFullName.setText(intentName)
        binding.etMobileNumber.setText(intentMobile)
        binding.etEmailId.setText(intentEmail)
        binding.etHouseNo.setText(intent.getStringExtra("house_number"))
        binding.etRoadArea.setText(intent.getStringExtra("road_name"))
        binding.etCity.setText(intent.getStringExtra("city"))
        binding.etState.setText(intent.getStringExtra("state"))
        binding.etPincode.setText(intent.getStringExtra("pincode"))

        when (intentAddressType) {
            "Home" -> binding.rbHome.isChecked = true
            "Work" -> binding.rbWork.isChecked = true
        }

        binding.tvMyLocation.setOnClickListener {
            getCurrentLocation()
        }

        binding.btUpdateAddress.setOnClickListener {
            val updatedType = when {
                binding.rbHome.isChecked -> "Home"
                binding.rbWork.isChecked -> "Work"
                else -> storedAddressType
            }

            val editedAddress = EditAddresses(
                mobile_number = binding.etMobileNumber.text.toString(),
                name = binding.etFullName.text.toString(),
                type = updatedType.toString(),
                house_number = binding.etHouseNo.text.toString(),
                city = binding.etCity.text.toString(),
                pincode = binding.etPincode.text.toString(),
                road_name = binding.etRoadArea.text.toString(),
                state = binding.etState.text.toString(),
                email = binding.etEmailId.text.toString()
            )

            editAddress(userId, editedAddress)
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
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
                100
            )
            return
        }

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    if (location != null) {
                        val geoCoder = Geocoder(this, Locale.getDefault())
                        val addresses =
                            geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            binding.etState.setText(address.adminArea ?: "")
                            binding.etCity.setText(address.locality ?: "")
                            binding.etPincode.setText(address.postalCode ?: "")
                            binding.etHouseNo.setText(address.featureName ?: "")
                            binding.etRoadArea.setText(
                                listOfNotNull(
                                    address.subLocality,
                                    address.thoroughfare
                                ).joinToString(", ")
                            )
                        } else {
                            showToast("Unable to get address")
                        }
                    } else {
                        showToast("Failed to get location")
                    }
                } else {
                    showToast("Failed to get location: ${task.exception?.message}")
                }
            }
    }

    private fun editAddress(userId: Int, editAddress: EditAddresses) {
        RetrofitInstance.apiService.editAddress(userId, editAddress)
            .enqueue(object : Callback<EditAddresses?> {
                override fun onResponse(
                    call: Call<EditAddresses?>,
                    response: Response<EditAddresses?>
                ) {
                    if (response.isSuccessful) {
                        with(sharedPref.edit()) {
                            putString("name", editAddress.name)
                            putString("mobile", editAddress.mobile_number)
                            putString("email", editAddress.email)
                            putString("house_number", editAddress.house_number)
                            putString("road_name", editAddress.road_name)
                            putString("city", editAddress.city)
                            putString("state", editAddress.state)
                            putString("pincode", editAddress.pincode)
                            putString("addressType", editAddress.type)
                            apply()
                        }
                        showToast("Updated Successfully")
                        finish()
                    } else {
                        showToast("Edit Failed: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EditAddresses?>, t: Throwable) {
                    showToast("Network Error: ${t.message}")
                }
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
