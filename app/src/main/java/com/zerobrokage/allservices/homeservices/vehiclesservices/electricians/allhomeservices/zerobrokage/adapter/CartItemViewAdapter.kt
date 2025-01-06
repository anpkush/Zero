package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CartItemViewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartViewApi

class CartItemViewAdapter(
    private var cartItems: List<CartViewApi.Data>,
    private val context: Context
) : RecyclerView.Adapter<CartItemViewAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(val binding: CartItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var quantity: Int = 1

        fun bind(item: CartViewApi.Data) {
            binding.apply {
                tvServiceName.text = item.name
                tvDetails.text = item.description
                textViewNumber.text = item.qty.toString()

                quantity = item.qty

                buttonPlus.setOnClickListener {
                    if (quantity < 10) {
                        quantity++
                        textViewNumber.text = quantity.toString()
                        item.qty = quantity
                    } else {
                        Toast.makeText(context, "Maximum quantity reached!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                buttonMinus.setOnClickListener {
                    if (quantity > 1) {
                        quantity--
                        textViewNumber.text = quantity.toString()
                        item.qty = quantity
                    } else {
                        Toast.makeText(context, "Minimum quantity is 1!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = CartItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val item = cartItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateData(newCartItems: List<CartViewApi.Data>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
}
