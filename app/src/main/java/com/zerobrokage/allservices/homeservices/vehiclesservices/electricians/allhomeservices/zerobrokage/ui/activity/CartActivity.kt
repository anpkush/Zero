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
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartData
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
    private val cartItems = mutableListOf<CartData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getInt("userId", 0)

        setupUI()
        getCartItems()

        binding.btPlace.setOnClickListener {
            if (cartItems.isNotEmpty()) {
                val intent = Intent(this, BookingActivity::class.java).apply {
                    putParcelableArrayListExtra("cartItems", ArrayList(cartItems))
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Cart is empty, please add items to place an order.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupUI() {
        binding.toolbar.ivBack.setOnClickListener { finish() }
        binding.toolbar.tvTitle.text = "Cart"

        cartAdapter = CartItemViewAdapter(
            mutableListOf(),
            this,
            this,
            userId,
            ::updateCartBadge
        )

        binding.rvGetItem.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(this@CartActivity)
        }
    }

    private fun getCartItems() {
        RetrofitInstance.apiService.cartViewApi(userId).enqueue(object : Callback<CartViewApi> {
            override fun onResponse(call: Call<CartViewApi>, response: Response<CartViewApi>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    cartItems.clear()
                    cartItems.addAll(data)
                    cartAdapter.updateData(cartItems)
                    updateCartBadge(cartItems.size)
                    toggleEmptyCartView(cartItems.isEmpty())
                } else {
                    showErrorAndClearCart("Cart is empty")
                }
            }

            override fun onFailure(call: Call<CartViewApi>, t: Throwable) {
                showErrorAndClearCart(t.localizedMessage)
            }
        })
    }

    private fun showErrorAndClearCart(message: String) {
        cartItems.clear()
        cartAdapter.updateData(cartItems)
        updateCartBadge(0)
        toggleEmptyCartView(true)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun toggleEmptyCartView(isEmpty: Boolean) {
        binding.ivCartEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvGetItem.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun updateCartBadge(count: Int) {
        binding.btPlace.isEnabled = count > 0
        binding.btPlace.alpha = if (count > 0) 1.0f else 0.5f
    }

    override fun onItemClick(id: Int, name: String, icon: String) {

    }


}
