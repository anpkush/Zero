package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ServicesItemviewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubCatItemData

class ServicesAdapter(
    private val subCatArrayList: List<SubCatItemData>, private val mListener: ItemClickListener) : RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val binding = ServicesItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServicesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subCatArrayList.size
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        val currentItem = subCatArrayList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            mListener.onItemClick(currentItem.id, currentItem.name)
        }
    }

    class ServicesViewHolder(private val binding: ServicesItemviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SubCatItemData) {
            binding.tvServicesName.text = data.name
            Glide.with(binding.ivServiceCategory.context)
                .load(data.icon)
                .circleCrop()
                .into(binding.ivServiceCategory)

        }
    }
}

interface ItemClickListener {
    fun onItemClick(id: Int, name: String)
}