package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.BookingStatusCardviewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubCatItemData

class BookingAdapter(private val subCatList: List<SubCatItemData>) :
    RecyclerView.Adapter<BookingAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: BookingStatusCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: SubCatItemData) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            BookingStatusCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subCatList[position])
    }
}
