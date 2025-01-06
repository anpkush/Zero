package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.CartItemViewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartViewApi

class CartItemViewAdapter(
    private var cartItems: MutableList<CartViewApi.Data>,
    private val context: Context
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

                buttonPlus.setOnClickListener {
                    if (item.qty < 10) {
                        item.qty++
                        textViewNumber.text = item.qty.toString()
                        notifyItemChanged(adapterPosition)
                    } else {
                        Toast.makeText(context, "Maximum quantity reached!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                buttonMinus.setOnClickListener {
                    if (item.qty > 1) {
                        item.qty--
                        textViewNumber.text = item.qty.toString()
                        notifyItemChanged(adapterPosition)
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
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateData(newCartItems: List<CartViewApi.Data>) {
        cartItems.clear()
        cartItems.addAll(newCartItems)
        notifyDataSetChanged()
    }
}
