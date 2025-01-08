package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.CartItemViewAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityCartBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartViewApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.ItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var binding: ActivityCartBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var cartAdapter: CartItemViewAdapter
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getInt("id", 0)

        setupUI()
        getCartItems()
    }

    private fun setupUI() {
        binding.toolbar.ivBack.setOnClickListener { finish() }
        binding.toolbar.tvTitle.text = "Cart"

        cartAdapter = CartItemViewAdapter(mutableListOf(), this, this, userId)
        binding.rvGetItem.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(this@CartActivity)
        }
    }

    private fun getCartItems() {
        RetrofitInstance.apiService.cartViewApi(userId).enqueue(object : Callback<CartViewApi> {
            override fun onResponse(call: Call<CartViewApi>, response: Response<CartViewApi>) {
                if (response.isSuccessful && response.body()?.data != null) {
                    val cartItems = response.body()?.data ?: emptyList()
                    cartAdapter.updateData(cartItems)
                    enablePlaceButton()
                } else {
                    Toast.makeText(this@CartActivity, "Cart is empty", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CartViewApi>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun enablePlaceButton() {
        binding.btPlace.visibility = View.VISIBLE
        binding.btPlace.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getCartItems()
    }

    override fun onItemClick(id: Int, name: String, icon: String) {
        Toast.makeText(this, "Clicked on $name", Toast.LENGTH_SHORT).show()
    }
}
