package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.SubMenuItemAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivitySubMenuBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubMenuListData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.ItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubMenuActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var binding: ActivitySubMenuBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val userId = intent.getIntExtra("userId", 0).takeIf { it != 0 }
            ?: sharedPref.getInt("userId", 0)

        if (userId == 0) {
            Toast.makeText(this, "Invalid user session. Please log in again.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val id = intent.getIntExtra("id", 0)
        val name = intent.getStringExtra("name") ?: "Unknown Service"

        binding.toolbar.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.toolbar.ivCart.visibility = View.VISIBLE

        binding.toolbar.ivCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        getSubMenuApi(id, name, userId)
    }

    private fun getSubMenuApi(id: Int, name: String, userId: Int) {
        RetrofitInstance.apiService.getSubMenuListData(id)
            .enqueue(object : Callback<SubMenuListData?> {
                override fun onResponse(
                    call: Call<SubMenuListData?>,
                    response: Response<SubMenuListData?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val subMenuList = response.body()?.submenus_data ?: emptyList()
                        val myAdapter = SubMenuItemAdapter(
                            subMenuList, this@SubMenuActivity, this@SubMenuActivity, userId
                        )
                        if (subMenuList.isNotEmpty()) {
                            binding.toolbar.tvTitle.text = name
                            binding.rvServicesSubMenuItem.apply {
                                layoutManager = LinearLayoutManager(this@SubMenuActivity)
                                adapter = myAdapter
                            }
                        } else {
                            Toast.makeText(
                                this@SubMenuActivity,
                                "No submenu available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@SubMenuActivity,
                            "Failed to retrieve services",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SubMenuListData?>, t: Throwable) {
                    Toast.makeText(
                        this@SubMenuActivity,
                        t.message ?: "Error while fetching data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onItemClick(id: Int, name: String, icon: String) {
    }
}
