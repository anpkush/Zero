package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.R
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ServicesItemviewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubCatItemData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.ItemClickListener

class AllServicesAdapter(
    private val subCatList: List<SubCatItemData>,
    private val mListener: ItemClickListener
) : RecyclerView.Adapter<AllServicesAdapter.MyAllServicesViewHolder>() {

    private var selectedPosition: Int = -1
    private var previousSelectedPosition: Int = -1

    class MyAllServicesViewHolder(private val binding: ServicesItemviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: SubCatItemData, isSelected: Boolean) {
            binding.tvServicesName.text = currentItem.name
            Glide.with(binding.ivServiceCategory.context).load(currentItem.icon).into(binding.ivServiceCategory)
            if (isSelected) {
                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.cadet_blue))
            } else {
                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, android.R.color.transparent))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAllServicesViewHolder {
        val binding = ServicesItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyAllServicesViewHolder(binding)
    }

    override fun getItemCount(): Int = subCatList.size

    override fun onBindViewHolder(holder: MyAllServicesViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentItem = subCatList[position]

        holder.bind(currentItem, position == selectedPosition)

        holder.itemView.setOnClickListener {
            if (selectedPosition != position) {
                previousSelectedPosition = selectedPosition
                selectedPosition = position

                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedPosition)

                mListener.onItemClick(currentItem.id, currentItem.name, currentItem.icon)
            }
        }
    }

    fun setSelectedPosition(position: Int) {
        previousSelectedPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousSelectedPosition)
        notifyItemChanged(selectedPosition)
    }
}
