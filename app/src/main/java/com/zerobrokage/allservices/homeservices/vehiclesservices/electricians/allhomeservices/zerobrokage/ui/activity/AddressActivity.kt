package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.ItemClickListener
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.SavedAddressesAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityAddressBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SavedAddressesApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var binding: ActivityAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("id", 0)
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.tvTitle.text = "My Addresses"

        binding.cdNewAdd.setOnClickListener {
            val intent = Intent(this, AddAddressActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        //Saved Addresses Api
        getSavedAddressesApi()
    }

    private fun getSavedAddressesApi() {
        val userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("id", 0)

        RetrofitInstance.apiService.savedAddresses(userId)
            .enqueue(object : Callback<SavedAddressesApi?> {
                override fun onResponse(
                    call: Call<SavedAddressesApi?>,
                    response: Response<SavedAddressesApi?>
                ) {
                    if (response.isSuccessful && response.body()?.data != null) {
                        val savedAddresses = response.body()?.data ?: emptyList()
                        if (savedAddresses.isNotEmpty()) {
                            binding.rvSavedAddress.apply {
                                adapter =
                                    SavedAddressesAdapter(savedAddresses, this@AddressActivity)
                                layoutManager = LinearLayoutManager(this@AddressActivity)
                            }
                        } else {
                            Toast.makeText(
                                this@AddressActivity,
                                "No addresses available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@AddressActivity,
                            "Failed to fetch addresses",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SavedAddressesApi?>, t: Throwable) {
                    Toast.makeText(this@AddressActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getSavedAddressesApi()
    }

    override fun onItemClick(id: Int, name: String) {

    }
}


