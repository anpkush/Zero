package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.Manifest
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
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.R
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityUpdateAddressBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.EditAddresses
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class UpdateAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateAddressBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.toolbar.tvTitle.text = "Update Address"
        binding.toolbar.ivBack.setOnClickListener { onBackPressed() }

        binding.tvMyLocation.setOnClickListener {
            getCurrentLocation()
        }
        intent.getIntExtra("id", 0)
        binding.etFullName.setText(intent.getStringExtra("name"))
        binding.etMobileNumber.setText(intent.getStringExtra("mobileNo"))
        binding.etHouseNo.setText(intent.getStringExtra("house_number"))
        binding.etRoadArea.setText(intent.getStringExtra("road_name"))
        binding.etCity.setText(intent.getStringExtra("city"))
        binding.etState.setText(intent.getStringExtra("state"))
        binding.etPincode.setText(intent.getStringExtra("pincode"))
        binding.etEmailId.setText(intent.getStringExtra("email"))

        binding.btUpdateAddress.setOnClickListener {
            val updatedType = when (binding.rdAddressType.checkedRadioButtonId) {
                R.id.rbHome -> "Home"
                R.id.rbWork -> "Work"
                else -> "Other"
            }

            val editedAddress = EditAddresses(
                mobile_number = binding.etMobileNumber.text.toString(),
                name = binding.etFullName.text.toString(),
                type = updatedType,
                house_number = binding.etHouseNo.text.toString(),
                city = binding.etCity.text.toString(),
                pincode = binding.etPincode.text.toString(),
                road_name = binding.etRoadArea.text.toString(),
                state = binding.etState.text.toString(),
                email = binding.etEmailId.text.toString()
            )

            // Call editAddress API
            editAddress(2, editedAddress)
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
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
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val geoCoder = Geocoder(this, Locale.getDefault())
                        val addresses = geoCoder.getFromLocation(latitude, longitude, 1)

                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            binding.etState.setText(address.adminArea ?: "")
                            binding.etCity.setText(address.locality ?: "")
                            binding.etPincode.setText(address.postalCode ?: "")
                            binding.etHouseNo.setText(address.featureName ?: "")
                            binding.etRoadArea.setText(listOfNotNull(address.subLocality, address.thoroughfare).joinToString(", "))
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        }
    }

    private fun editAddress(id: Int, editAddress: EditAddresses) {
        RetrofitInstance.apiService.editAddress(id, editAddress)
            .enqueue(object : Callback<EditAddresses?> {
                override fun onResponse(
                    call: Call<EditAddresses?>,
                    response: Response<EditAddresses?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
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