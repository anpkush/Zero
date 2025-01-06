package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.BookingItemviewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CustomDialogboxBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubmenusData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.ItemClickListener
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.ViewCartBottomFragment
import retrofit2.Call
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SubMenuItemAdapter(
    private var submenus: List<SubmenusData>,
    private val mListener: ItemClickListener,
    private val context: Context,
    private val userId: Int
) : RecyclerView.Adapter<SubMenuItemAdapter.MySubMenuViewHolder>() {

    private val cartItems: MutableMap<Int, Int> = mutableMapOf()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)

    init {
        loadCartDataFromSharedPreferences()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySubMenuViewHolder {
        val binding = BookingItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MySubMenuViewHolder(binding, context, this, userId)
    }

    override fun getItemCount(): Int = submenus.size

    override fun onBindViewHolder(holder: MySubMenuViewHolder, position: Int) {
        val submenuData = submenus[position]
        holder.bind(submenuData)
        holder.itemView.setOnClickListener {
            mListener.onItemClick(submenuData.id, submenuData.name, submenuData.image)
        }
    }

    fun addToCart(id: Int, number: Int) {
        cartItems[id] = number
        saveCartDataToSharedPreferences()
    }

    private fun saveCartDataToSharedPreferences() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val cartJson = gson.toJson(cartItems)
        editor.putString("cartItems", cartJson)
        editor.apply()
    }

    private fun loadCartDataFromSharedPreferences() {
        val gson = Gson()
        val cartJson = sharedPreferences.getString("cartItems", null)
        if (cartJson != null) {
            val type = object : TypeToken<MutableMap<Int, Int>>() {}.type
            val savedCartItems: MutableMap<Int, Int> = gson.fromJson(cartJson, type)
            cartItems.putAll(savedCartItems)
        }
    }

    class MySubMenuViewHolder(
        private val binding: BookingItemviewBinding,
        private val context: Context,
        private val adapter: SubMenuItemAdapter,
        private val userId: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        private var number = 1
        private var isAddedToCart = false

        fun bind(data: SubmenusData) {
            binding.tvTrendingService.text = data.name
            binding.tvLocationName.text = data.city
            binding.tvTrendingDetails.text = data.description

            Glide.with(binding.ivTrendingCategory.context).load(data.image).into(binding.ivTrendingCategory)

            binding.tvViewDetails.setOnClickListener {
                alertDialog(data)
            }

            updateUI()

            binding.buttonPlus.setOnClickListener {
                if (number < 10) {
                    number++
                    updateUI()
                } else {
                    Toast.makeText(context, "Maximum quantity reached!", Toast.LENGTH_SHORT).show()
                }
            }

            binding.buttonMinus.setOnClickListener {
                if (number > 1) {
                    number--
                    updateUI()
                }
            }

            binding.btAddCart.setOnClickListener {
                if (!isAddedToCart && number > 0) {
                    val cartApi = CartApi(sub_menu_id = data.id.toString(), qty = number)
                    addCartApi(userId, cartApi)
                    adapter.addToCart(data.id, number)
                    showViewCartBottomFragment()
                    isAddedToCart = true
                    updateUI()
                } else if (isAddedToCart) {
                    Toast.makeText(context, "Item already added to cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Quantity must be greater than 0!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun showViewCartBottomFragment() {
            val fragment = ViewCartBottomFragment.newInstance(adapter.cartItems)
            (context as? FragmentActivity)?.supportFragmentManager?.let {
                fragment.show(it, "ViewCartBottomFragment")
            }
        }

        private fun updateUI() {
            binding.textViewNumber.text = number.toString()

            if (isAddedToCart) {
                binding.buttonPlus.isEnabled = false
                binding.buttonMinus.isEnabled = false
                binding.btAddCart.isEnabled = false
                binding.btAddCart.text = "Remove"
            } else {
                binding.btAddCart.isEnabled = true
            }
        }

        private fun addCartApi(userId: Int, cartApi: CartApi) {
            RetrofitInstance.apiService.addToCart(userId, cartApi)
                .enqueue(object : retrofit2.Callback<CartApi> {
                    override fun onResponse(call: Call<CartApi>, response: retrofit2.Response<CartApi>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Item added to cart successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to add item to cart: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<CartApi>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        private fun alertDialog(data: SubmenusData) {
            val builder = AlertDialog.Builder(context)
            val binding = CustomDialogboxBinding.inflate(LayoutInflater.from(context))
            builder.setView(binding.root)

            val alertDialog = builder.create()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvAllDetails.text = Html.fromHtml(data.details, Html.FROM_HTML_MODE_COMPACT)
            } else {
                binding.tvAllDetails.text = Html.fromHtml(data.details)
            }

            binding.ivClose.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }
}
