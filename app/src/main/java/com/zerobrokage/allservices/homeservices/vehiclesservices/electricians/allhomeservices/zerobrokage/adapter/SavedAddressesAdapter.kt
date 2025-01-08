package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.UpdateAddressActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.AddressViewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SavedAddressApi


class SavedAddressesAdapter(
    private val addressList: List<SavedAddressApi.Addresse>,
    private val mListener: ItemClickListener
) : RecyclerView.Adapter<SavedAddressesAdapter.AddressViewHolder>() {

    private var selectedPosition: Int = -1

    inner class AddressViewHolder(val binding: AddressViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SavedAddressApi.Addresse, position: Int) {
            binding.tvUserName.text = data.name
            binding.tvUserMobileNo.text = data.mobile_number
            binding.workLabel.text = data.type
            binding.tvUserAddresses.text = "${data.house_number}, ${data.road_name}, ${data.city}, ${data.state}, ${data.pincode}"


            binding.ivEdit.setOnClickListener {
                val context = it.context
                val intent = Intent(context, UpdateAddressActivity::class.java).apply {
                    putExtra("id", position)
                    putExtra("name", data.name)
                    putExtra("type", data.type)
                    putExtra("mobileNo", data.mobile_number)
                    putExtra("house_number", data.house_number)
                    putExtra("road_name", data.road_name)
                    putExtra("city", data.city)
                    putExtra("state", data.state)
                    putExtra("pincode", data.pincode)
                    putExtra("email", data.email)
                }
                context.startActivity(intent)
            }

            binding.ivDelete.setOnClickListener {
                mListener.onItemClick(position, data.name)
                Toast.makeText(it.context, "Delete", Toast.LENGTH_SHORT).show()
            }

            itemView.setOnClickListener {
                mListener.onItemClick(position, data.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = AddressViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val addData = addressList[position]
        holder.bind(addData, position)
    }

    override fun getItemCount(): Int = addressList.size
}
