package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubCatItemData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.MenuCardItemviewBinding

class TrendingAdapter(
    private val subCatArrayList: List<SubCatItemData>, private val mListener: ItemClickListener
) : RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {

    private val trendingItems = subCatArrayList.filter { it.trending == 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val binding =
            MenuCardItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return trendingItems.size
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val currentItem = trendingItems[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            mListener.onItemClick(currentItem.id, currentItem.name)
        }

    }

    inner class TrendingViewHolder(private val binding: MenuCardItemviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SubCatItemData) {
            binding.tvMenuService.text = data.name
            Glide.with(binding.ivMenuServices.context)
                .load(data.icon)
                .centerCrop()
                .into(binding.ivMenuServices)
        }
    }
}
