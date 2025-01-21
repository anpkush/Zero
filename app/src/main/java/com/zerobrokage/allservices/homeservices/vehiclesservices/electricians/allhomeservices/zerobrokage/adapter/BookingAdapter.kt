package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.BookingStatusCardviewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.Booking
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.BookingDetailsActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.BookingDetailsFragment

class BookingAdapter(private var bookingList: List<Booking>) :
    RecyclerView.Adapter<BookingAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: BookingStatusCardviewBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(currentBooking: Booking) {
            binding.tvServiceName.text = currentBooking.service_name
            Glide.with(binding.root.context)
                .load(currentBooking.service_image)
                .into(binding.ivServicesPic)

            binding.qtyCount.text = currentBooking.qty.toString()
            binding.status.text = currentBooking.status
            binding.tvDate.text = "${currentBooking.booking_date} || ${currentBooking.booking_time}"

            binding.btViewMore.setOnClickListener{
                val intent = Intent(it.context, BookingDetailsActivity::class.java)
                intent.putExtra("currentBookingId",currentBooking.id)
                intent.putExtra("bookingServicesName",currentBooking.service_name)
                intent.putExtra("bookingServicesQty",currentBooking.qty)
                intent.putExtra("bookingServicesImage",currentBooking.service_image)
                intent.putExtra("bookingCustomerName",currentBooking.name)
                intent.putExtra("bookingCustomerAdd",currentBooking.full_address)
                intent.putExtra("bookingCustomerMobileNumber",currentBooking.mobile_number)
                intent.putExtra("bookingStatus",currentBooking.status)
                it.context.startActivity(intent)
            }

            when (currentBooking.status) {
                "pending" -> binding.status.setTextColor(binding.root.context.getColor(android.R.color.holo_red_light))
                "complete" -> binding.status.setTextColor(binding.root.context.getColor(android.R.color.holo_green_dark))
                else -> binding.status.setTextColor(binding.root.context.getColor(android.R.color.holo_blue_dark))
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = BookingStatusCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val booking = bookingList[position]
        holder.bind(booking)
    }

    fun updateData(newList: List<Booking>) {
        bookingList = newList
        notifyDataSetChanged()
    }
}


