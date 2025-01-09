package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.UpdateAddressActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.AddressViewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.DeleteApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SavedAddressApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SavedAddressesAdapter(
    private var addressList: MutableList<SavedAddressApi.Addresse>,
    private val mListener: ItemClickListener,
    private val context: Context,
    private val userId: Int,
    private val cartEmptyCallback: (Boolean) -> Unit
) : RecyclerView.Adapter<SavedAddressesAdapter.AddressViewHolder>() {


    inner class AddressViewHolder(val binding: AddressViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SavedAddressApi.Addresse, position: Int) {
            binding.tvUserName.text = data.name
            binding.tvUserMobileNo.text = data.mobile_number
            binding.workLabel.text = data.type
            binding.tvUserAddresses.text =
                "${data.house_number}, ${data.road_name}, ${data.city}, ${data.state}, ${data.pincode}"

            binding.ivEdit.setOnClickListener {
                val intent = Intent(context, UpdateAddressActivity::class.java).apply {
                    putExtra("id", data.id)
                    putExtra("name", data.name)
                    putExtra("type", data.type)
                    putExtra("mobileNo", data.mobile_number)
                    putExtra("house_number", data.house_number)
                    putExtra("road_name", data.road_name)
                    putExtra("city", data.city)
                    putExtra("state", data.state)
                    putExtra("pincode", data.pincode)
                    putExtra("email", data.email)
                    putExtra("userId", userId)
                }
                context.startActivity(intent)
            }

            binding.ivDelete.setOnClickListener {
                deleteAddress(userId, data.id, position)
            }

            itemView.setOnClickListener {
                mListener.onItemClick(position, data.name)
            }
        }

        private fun deleteAddress(userId: Int, addressId: Int, position: Int) {
            RetrofitInstance.apiService.deleteAddress(userId).enqueue(object : Callback<DeleteApi> {
                override fun onResponse(
                    call: Call<DeleteApi>,
                    response: Response<DeleteApi>
                ) {
                    if (response.isSuccessful) {
                        addressList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, addressList.size)
                        Toast.makeText(context, "Address removed successfully", Toast.LENGTH_SHORT).show()

                        cartEmptyCallback(addressList.isNotEmpty())
                    } else {
                        Toast.makeText(context, "Failed to delete address", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DeleteApi>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
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
