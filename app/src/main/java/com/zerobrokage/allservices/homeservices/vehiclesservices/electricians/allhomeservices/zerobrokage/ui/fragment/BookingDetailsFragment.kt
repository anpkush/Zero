package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.BookingAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.FragmentDetailsBookingBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.BookingListApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingDetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBookingBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBookingBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tvBooking.text = "Booking List"


        sharedPref = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userId = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("id", 0)

        if (userId != 0) {
            getBookingList(userId)
        } else {
            Toast.makeText(context, "User ID is not available", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun getBookingList(userId: Int) {
        RetrofitInstance.apiService.getBookingList(userId).enqueue(object : Callback<BookingListApi> {
            override fun onResponse(call: Call<BookingListApi>, response: Response<BookingListApi>) {
                if (response.isSuccessful) {
                    val bookingData = response.body()?.data?.bookings ?: emptyList()

                    binding.rvbooking.layoutManager = LinearLayoutManager(context)
                    binding.rvbooking.adapter = BookingAdapter(bookingData)
                } else {
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookingListApi>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
