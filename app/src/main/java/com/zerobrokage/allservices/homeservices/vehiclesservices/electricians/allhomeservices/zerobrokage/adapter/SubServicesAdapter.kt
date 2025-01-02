package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ServicesAllItemviewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.Data
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.SubMenuActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.ServicesFragment

class SubServicesAdapter(
    private val serviceMenuData: List<Data>,
    private val mListener: ServicesFragment
) : RecyclerView.Adapter<SubServicesAdapter.MyViewHolder>() {

    class MyViewHolder(private var binding: ServicesAllItemviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuData: Data) {
            binding.tvAllMenuService.text = menuData.name
            Glide.with(binding.ivAllMenuServices.context)
                .load(menuData.icon)
                .centerInside()
                .into(binding.ivAllMenuServices)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ServicesAllItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return serviceMenuData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuData = serviceMenuData[position]
        holder.bind(menuData)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, SubMenuActivity::class.java).apply {
                putExtra("id", menuData.id)
                putExtra("name", menuData.name)
            }
            it.context.startActivity(intent)

        }
    }
}
