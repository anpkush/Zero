package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.CartItemViewAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityCartBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartViewApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var cartAdapter: CartItemViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.ivBack.setOnClickListener { finish() }
        binding.toolbar.tvTitle.text = "Cart"

        cartAdapter = CartItemViewAdapter(mutableListOf(), this)
        binding.rvGetItem.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(this@CartActivity)
        }

        binding.btPlace.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        getCartItem()
    }

    private fun getCartItem() {
        sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userId = sharedPref.getInt("id", 0)

        RetrofitInstance.apiService.cartViewApi(userId).enqueue(object : Callback<CartViewApi> {
            override fun onResponse(call: Call<CartViewApi>, response: Response<CartViewApi>) {
                if (response.isSuccessful && response.body()?.data != null) {
                    val cartItems = response.body()?.data ?: emptyList()
                    cartAdapter.updateData(cartItems)
                } else {
                    Toast.makeText(this@CartActivity, "Cart is empty", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CartViewApi>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getCartItem()
    }
}
