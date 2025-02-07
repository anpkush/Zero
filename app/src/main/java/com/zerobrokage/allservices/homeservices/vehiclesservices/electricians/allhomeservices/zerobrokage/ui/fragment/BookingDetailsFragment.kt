package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment

import BookingAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBookingBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tvBooking.text = "Booking List"

        sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", 0)

        if (userId != 0) {
            getBookingList(userId)
        } else {
            Toast.makeText(context, "User ID is not available", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun getBookingList(userId: Int) {
        if (isAdded) {
            RetrofitInstance.apiService.getBookingList(userId).enqueue(object : Callback<BookingListApi> {
                override fun onResponse(call: Call<BookingListApi>, response: Response<BookingListApi>) {
                    if (isAdded) {
                        if (response.isSuccessful) {
                            val bookingData = response.body()?.data?.bookings ?: emptyList()
                            binding.rvbooking.layoutManager = LinearLayoutManager(context)
                            val adapter = BookingAdapter(bookingData)
                            binding.rvbooking.adapter = adapter
                        } else {
                            Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<BookingListApi>, t: Throwable) {
                    if (isAdded) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
