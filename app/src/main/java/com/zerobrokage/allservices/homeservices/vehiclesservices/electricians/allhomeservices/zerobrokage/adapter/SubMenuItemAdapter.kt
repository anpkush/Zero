package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.BookingItemviewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CustomDialogboxBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubmenusData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.ItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubMenuItemAdapter(
    private var submenus: List<SubmenusData>,
    private val mListener: ItemClickListener,
    private val context: Context,
    private val userId: Int
) : RecyclerView.Adapter<SubMenuItemAdapter.MySubMenuViewHolder>() {

    private val cartItems: MutableMap<Int, Int> = mutableMapOf()
    private val sharedPreferences = context.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)

    init {
        loadCartDataFromSharedPreferences()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySubMenuViewHolder {
        val binding =
            BookingItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        notifyDataSetChanged()
    }

    fun removeFromCart(id: Int) {
        cartItems.remove(id)
        saveCartDataToSharedPreferences()
        notifyDataSetChanged()
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
        private lateinit var data: SubmenusData

        fun bind(data: SubmenusData) {
            this.data = data
            isAddedToCart = adapter.cartItems.containsKey(data.id)
            number = adapter.cartItems[data.id] ?: 1
            binding.tvTrendingService.text = data.name
            binding.tvLocationName.text = data.city
            binding.tvTrendingDetails.text = data.description
            Glide.with(binding.ivTrendingCategory.context).load(data.image)
                .into(binding.ivTrendingCategory)
            updateUI()

            binding.btAddCart.setOnClickListener {
                if (!isAddedToCart) {
                    number = 1
                    addCartApi(userId, CartApi(sub_menu_id = data.id.toString(), qty = number))
                    adapter.addToCart(data.id, number)
                    updateUI()
                }
            }

            binding.buttonPlus.setOnClickListener {
                if (number < 10) {
                    number++
                    addCartApi(userId, CartApi(sub_menu_id = data.id.toString(), qty = 1))
                    adapter.addToCart(data.id, number)
                    updateUI()
                } else {
                    Toast.makeText(context, "Maximum quantity reached!", Toast.LENGTH_SHORT).show()
                }
            }

            binding.buttonMinus.setOnClickListener {
                if (number > 1) {
                    number--
                    addCartApi(userId, CartApi(sub_menu_id = data.id.toString(), qty = -1))
                    adapter.addToCart(data.id, number)
                    updateUI()
                } else if (number == 1) {
                    number = 0
                    adapter.removeFromCart(data.id)
                    removeCartApi(userId, data.id)
                }

            }


            binding.tvViewDetails.setOnClickListener {
                alertDialog(data)
            }
        }

        private fun removeCartApi(userId: Int, subMenuId: Int) {
            RetrofitInstance.apiService.deleteItem(userId, subMenuId)
                .enqueue(object : Callback<Map<String, Any>> {
                    override fun onResponse(
                        call: Call<Map<String, Any>>,
                        response: Response<Map<String, Any>>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Item removed from cart successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to remove item from cart: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        private fun updateUI() {
            binding.textViewNumber.text = number.toString()
            isAddedToCart = adapter.cartItems.containsKey(data.id) && number > 0

            if (isAddedToCart) {
                binding.btAddCart.visibility = View.GONE
                binding.counterLayout.visibility = View.VISIBLE
            } else {
                binding.btAddCart.visibility = View.VISIBLE
                binding.counterLayout.visibility = View.GONE
            }
        }

        private fun addCartApi(userId: Int, cartApi: CartApi) {
            RetrofitInstance.apiService.addToCart(userId, cartApi)
                .enqueue(object : Callback<CartApi> {
                    override fun onResponse(call: Call<CartApi>, response: Response<CartApi>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Item added to cart successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to add item to cart: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<CartApi>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        private fun alertDialog(data: SubmenusData) {
            val builder = AlertDialog.Builder(context)
            val dialogBinding = CustomDialogboxBinding.inflate(LayoutInflater.from(context))
            builder.setView(dialogBinding.root)
            val alertDialog = builder.create()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dialogBinding.tvAllDetails.text =
                    Html.fromHtml(data.details, Html.FROM_HTML_MODE_COMPACT)
            } else {
                dialogBinding.tvAllDetails.text = Html.fromHtml(data.details)
            }
            dialogBinding.ivClose.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }
}
