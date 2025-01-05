package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.R.attr.id
import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.BookingItemviewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CustomDialogboxBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubmenusData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.ItemClickListener
import retrofit2.Call


class SubMenuItemAdapter(
    private var submenus: List<SubmenusData>,
    private val mListener: ItemClickListener,
    private val context: Context,
    private val userId: Int
) : RecyclerView.Adapter<SubMenuItemAdapter.MySubMenuViewHolder>() {

    private val cartItems = mutableMapOf<Int, Int>()

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

    }

    class MySubMenuViewHolder(
        private val binding: BookingItemviewBinding,
        private val context: Context,
        private val adapter: SubMenuItemAdapter,
        private val userId: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        private var number = 1

        fun bind(data: SubmenusData) {
            binding.tvTrendingService.text = data.name
            binding.tvLocationName.text = data.city
            binding.tvTrendingDetails.text = data.description

            Glide.with(binding.ivTrendingCategory.context)
                .load(data.image)
                .into(binding.ivTrendingCategory)

            binding.tvViewDetails.setOnClickListener {
                showDetailsDialog(data)
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
                if (number > 0) {
                    val cartApi = CartApi(sub_menu_id = data.id.toString(), qty = number)
                    addCartApi(userId, cartApi)
                    adapter.addToCart(data.id, number)
                    Toast.makeText(context, "Added to cart: ${data.name}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Quantity must be greater than 0!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun updateUI() {
            binding.textViewNumber.text = number.toString()
            binding.btAddCart.isEnabled = number > 0
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

        private fun showDetailsDialog(data: SubmenusData) {
            val builder = AlertDialog.Builder(context)
            val dialogBinding = CustomDialogboxBinding.inflate(LayoutInflater.from(context))
            builder.setView(dialogBinding.root)

            val alertDialog = builder.create()

            dialogBinding.tvAllDetails.text =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(data.details, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(data.details)
                }

            dialogBinding.ivClose.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }
}
