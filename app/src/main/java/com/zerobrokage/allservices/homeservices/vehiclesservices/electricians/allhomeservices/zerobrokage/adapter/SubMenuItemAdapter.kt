package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
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


class SubMenuItemAdapter(
    private var submenus: List<SubmenusData>,
    private val mListener: ItemClickListener,
    private val context: Context
) : RecyclerView.Adapter<SubMenuItemAdapter.MySubMenuViewHolder>() {

    private val cartItems = mutableMapOf<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySubMenuViewHolder {
        val binding =
            BookingItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MySubMenuViewHolder(binding, context, this)
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
        private val adapter: SubMenuItemAdapter
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
                alertDialog(data)
            }

            binding.textViewNumber.text = number.toString()

            binding.btAddCart.isEnabled = number > 0
            binding.buttonPlus.setOnClickListener {
                if (number < 10) {
                    number++
                    binding.textViewNumber.text = number.toString()
                } else {
                    Toast.makeText(context, "Maximum quantity reached!", Toast.LENGTH_SHORT).show()
                }
            }

            binding.buttonMinus.setOnClickListener {
                if (number > 0) {
                    number--
                    updateUI()
                }
            }

            binding.btAddCart.setOnClickListener {
                if (number > 0) {
                    val cartApi = CartApi(
                        enquiries_id = data.id,
                        sub_menu_id = data.menu_id,
                        qty = number
                    )
                    addCartApi(cartApi)
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

        private fun addCartApi(data: CartApi) {
            val cartApi = CartApi(enquiries_id = data.enquiries_id, sub_menu_id = data.sub_menu_id, qty = number)

            RetrofitInstance.apiService.addToCart(cartApi).enqueue(object : retrofit2.Callback<CartApi> {
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

            binding.tvAllDetails.text =
                Html.fromHtml(data.details, Html.FROM_HTML_MODE_COMPACT)

            binding.ivClose.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        private fun showViewCartBottomFragment() {
            val fragment = ViewCartBottomFragment()
            (context as? FragmentActivity)?.supportFragmentManager?.let {
                fragment.show(it, "ViewCartBottomFragment")
            }
        }
    }
}
