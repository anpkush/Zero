package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CartItemViewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartViewApi

class CartItemViewAdapter(
    private var cartItems: List<CartViewApi.Data>,
    private val context: Context
) : RecyclerView.Adapter<CartItemViewAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(val binding: CartItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = CartItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val item = cartItems[position]
        holder.binding.apply {
            tvServiceName.text = item.name
            tvQtyCount.id = item.qty
            tvDetails.text = item.description
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateData(newCartItems: List<CartViewApi.Data>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
}
