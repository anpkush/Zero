package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.ItemClickListener
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.MenuCardItemviewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.Data

class BottomMenuAdapter(
    private val serviceMenuData: List<Data>,
    private val mListener: ItemClickListener
) : RecyclerView.Adapter<BottomMenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuCardItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = serviceMenuData.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuData = serviceMenuData[position]
        holder.bind(menuData)
        holder.itemView.setOnClickListener {
            mListener.onItemClick(menuData.id, menuData.name)
        }
    }

    class MenuViewHolder(private val binding: MenuCardItemviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menuData: Data) {
            binding.tvMenuService.text = menuData.name
            Glide.with(binding.ivMenuServices.context)
                .load(menuData.icon)
                .centerInside()
                .into(binding.ivMenuServices)
        }
    }
}

