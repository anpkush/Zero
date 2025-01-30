package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CartItemViewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.CartActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartItemViewAdapter(
    private var cartItems: MutableList<CartData>,
    private val context: Context,
    private val mListener: CartActivity,
    private val userId: Int,
    private val updateCartBadge: (Int) -> Unit
) : RecyclerView.Adapter<CartItemViewAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(private val binding: CartItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartData, position: Int) {
            Glide.with(binding.ivItemImage.context).load(item.image).into(binding.ivItemImage)
            binding.tvServiceName.text = item.name
            binding.tvDetails.text = item.description
            binding.textViewNumber.text = "Qty: ${item.qty}"

            binding.ivRemove.setOnClickListener {
                deleteItem(item.id, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = CartItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem, position)
        holder.itemView.setOnClickListener {
            mListener.onItemClick(cartItem.id, cartItem.name, cartItem.image)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateData(newCartItems: List<CartData>) {
        cartItems.clear()
        cartItems.addAll(newCartItems)
        notifyDataSetChanged()
        updateCartBadge(cartItems.size)
    }

    private fun deleteItem(cartItemId: Int, position: Int) {
        RetrofitInstance.apiService.deleteCartItem(userId, cartItemId)
            .enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if (response.isSuccessful && response.body()?.get("success") == true) {
                        cartItems.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, cartItems.size)
                        Toast.makeText(context, "Item removed successfully", Toast.LENGTH_SHORT).show()
                        updateCartBadge(cartItems.size)
                    } else {
                        Toast.makeText(context, "Failed to remove item. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    /*private fun updateQuantityOnServer(userId: Int, itemId: Int, quantity: Int) {
        val request = ItemUpdateRequest(adjustment = quantity.toString())
        RetrofitInstance.apiService.updateCartItem(userId, itemId, request)
            .enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Quantity updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update quantity. Try again later.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }*/
}
