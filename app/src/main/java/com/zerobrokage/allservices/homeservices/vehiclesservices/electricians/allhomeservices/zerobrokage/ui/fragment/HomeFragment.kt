package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment

import BottomSheetServicesFragment
import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.R
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.CustomerReviewAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.ItemClickListener
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.ServicesAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.TrendingAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.FragmentHomeBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubCatData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.CartActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class HomeFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                    fetchCurrentLocation()
                }

                else -> {
                    Toast.makeText(context, "Location permission is required", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        requestLocationPermission()

        val savedAddress = getAddressFromSharedPreferences()
        binding.tvLocation.text = savedAddress ?: "Fetching location..."

        binding.ivCart.setOnClickListener {
            val intent = Intent(context, CartActivity::class.java)
            startActivity(intent)
        }

        // Set up image slider
        imageSlider()

        // API Calls for All Services
        getApiData()

        return view
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun fetchCurrentLocation() {
        if (isLocationEnabled()) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude

                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        try {
                            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                            if (addresses?.isNotEmpty() == true) {
                                val address = addresses[0].getAddressLine(0)
                                binding.tvLocation.text = address ?: "Unable to get address"

                                saveAddressInSharedPreferences(address)
                            } else {
                                binding.tvLocation.text = "Unable to get address"
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            binding.tvLocation.text = "Geocoder service unavailable"
                        }
                    } else {
                        Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to get location: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Please enable location services", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Application.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun saveAddressInSharedPreferences(address: String) {
        val sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Application.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("UserAddress", address)
        editor.apply()
    }

    private fun getAddressFromSharedPreferences(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Application.MODE_PRIVATE)
        return sharedPreferences.getString("UserAddress", null)
    }

    private fun imageSlider() {
        val imageList = arrayListOf(
            SlideModel(R.drawable.acservice, "AC Services"),
            SlideModel(R.drawable.vehiclesss, "Vehicle Services"),
            SlideModel(R.drawable.electrician, "Electrician Services"),
            SlideModel(R.drawable.plumber, "Plumber Services")
        )
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
    }

    private fun getApiData() {
        RetrofitInstance.apiService.getSubCat().enqueue(object : Callback<SubCatData?> {
            override fun onResponse(call: Call<SubCatData?>, response: Response<SubCatData?>) {
                if (_binding != null) {
                    val subCatList = response.body()?.data ?: emptyList()
                    val myAdapter = ServicesAdapter(subCatList, this@HomeFragment)
                    binding.rvServices.apply {
                        layoutManager = GridLayoutManager(context, 3)
                        adapter = myAdapter
                    }
                    val myTrendingAdapter = fragmentManager?.let { TrendingAdapter(subCatList, this@HomeFragment) }
                    binding.rvTrendingCat.apply {
                        adapter = myTrendingAdapter
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    }
                } else {
                    Toast.makeText(context, "Server Issue", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SubCatData?>, t: Throwable) {
                Log.e("API_ERROR", "Failed to fetch subcategories", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(id: Int, name: String) {
        val bottomSheetFragment = BottomSheetServicesFragment.newInstance(id, name)
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
    }
}
