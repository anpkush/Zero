package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CartItemViewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartViewApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.ItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartItemViewAdapter(
    private var cartItems: MutableList<CartViewApi.Data>,
    private val context: Context,
    private val mListener: ItemClickListener,
    private val userId: Int
) : RecyclerView.Adapter<CartItemViewAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(private val binding: CartItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartViewApi.Data) {
            Glide.with(binding.ivItemImage.context)
                .load(item.image)
                .into(binding.ivItemImage)

            binding.apply {
                tvServiceName.text = item.name
                tvDetails.text = item.description
                textViewNumber.text = item.qty.toString()

                // Increment Quantity
                buttonPlus.setOnClickListener {
                    if (item.qty < 10) {
                        item.qty++
                        textViewNumber.text = item.qty.toString()
                       // updateQuantityOnServer(userId,item.id, item.qty)
                    } else {
                        Toast.makeText(context, "Maximum quantity reached!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                buttonMinus.setOnClickListener {
                    if (item.qty > 1) {
                        item.qty--
                        textViewNumber.text = item.qty.toString()
                        //updateQuantityOnServer(userId,item.id, item.qty)
                    } else {
                        Toast.makeText(context, "Minimum quantity is 1!", Toast.LENGTH_SHORT).show()
                    }
                }

                // Remove Item
                ivRemove.setOnClickListener {
                    deleteItem(item.id, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = CartItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
        holder.itemView.setOnClickListener {
            mListener.onItemClick(cartItem.id, cartItem.name, cartItem.image)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateData(newCartItems: List<CartViewApi.Data>) {
        cartItems.clear()
        cartItems.addAll(newCartItems)
        notifyDataSetChanged()
    }

    private fun deleteItem(cartItemId: Int, position: Int) {
        RetrofitInstance.apiService.deleteCartItem(userId, cartItemId)
            .enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
                    if (response.isSuccessful && response.body()?.get("success") == true) {
                        cartItems.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, cartItems.size)
                        Toast.makeText(context, "Item removed successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    /*private fun updateQuantityOnServer(userId: Int,cartItemId: Int, newQty: Int) {
        RetrofitInstance.apiService.updateCartQty(userId, cartItemId, newQty).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful && response.body()?.get("success") == true) {
                    Toast.makeText(context, "Quantity updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/
}
