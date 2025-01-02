package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.AllServicesAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.SubServicesAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.FragmentServicesBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.ServiceMenuData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubCatData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ItemClickListener {
    fun onItemClick(id: Int, name: String, icon: String)
}

class ServicesFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!

    private var selectedPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServicesBinding.inflate(inflater, container, false)

        binding.tvAllServices.text = "All Services"

        // Call API to get main service data
        getApiData()

        return binding.root
    }

    private fun getApiData() {
        RetrofitInstance.apiService.getSubCat().enqueue(object : Callback<SubCatData?> {
            override fun onResponse(call: Call<SubCatData?>, response: Response<SubCatData?>) {
                if (view == null || _binding == null) return

                if (response.isSuccessful && response.body() != null) {
                    val subCatData = response.body()

                    if (subCatData?.data != null && subCatData.data.isNotEmpty()) {
                        val subCatList = subCatData.data
                        val myAdapter = AllServicesAdapter(subCatList, this@ServicesFragment)

                        binding.rvAllServices.apply {
                            adapter = myAdapter
                            layoutManager = LinearLayoutManager(context)
                        }

                        selectedPosition = 0
                        myAdapter.setSelectedPosition(selectedPosition)

                        val item = subCatList[selectedPosition]
                        onItemClick(item.id, item.name, item.icon)
                    } else {
                        binding.rvAllServices.adapter = null
                        Toast.makeText(context, "No services available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    binding.rvAllServices.adapter = null
                    Toast.makeText(context, "Server Issue: ${response.code()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<SubCatData?>, t: Throwable) {
                Toast.makeText(context, "Server Issue", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getSubMenuApi(id: Int) {
        RetrofitInstance.apiService.getServicesMenuData(id)
            .enqueue(object : Callback<ServiceMenuData?> {
                override fun onResponse(
                    call: Call<ServiceMenuData?>,
                    response: Response<ServiceMenuData?>
                ) {

                    if (view == null || _binding == null) return
                    if (response.isSuccessful && response.body() != null) {
                        val menuList = response.body()?.data ?: emptyList()
                        if (menuList.isNotEmpty()) {
                            binding.rvSubServicesList.apply {
                                layoutManager = GridLayoutManager(context, 2)
                                adapter = SubServicesAdapter(menuList, this@ServicesFragment)
                                visibility = View.VISIBLE
                            }
                        } else {
                            binding.rvSubServicesList.adapter = null
                            binding.rvSubServicesList.visibility = View.GONE
                            Toast.makeText(context, "Server Issue", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        binding.rvSubServicesList.adapter = null
                        Toast.makeText(context, "No services available", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ServiceMenuData?>, t: Throwable) {
                    if (view == null || _binding == null) return
                    binding.rvSubServicesList.adapter = null
                    Toast.makeText(
                        context,
                        "Error occurred: ${t.localizedMessage ?: "Unknown error"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onItemClick(id: Int, name: String, icon: String) {
        binding.tvServicesName.text = name
        Glide.with(this)
            .load(icon)
            .into(binding.ivSubService)

        getSubMenuApi(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
