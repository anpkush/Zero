package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityBookingDetailsBinding

class BookingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookingId = intent.getIntExtra("currentBookingId", 0)
        val servicesName = intent.getStringExtra("bookingServicesName") ?: "Service Name"
        val qty = intent.getIntExtra("bookingServicesQty", 0)
        val serviceImage = intent.getStringExtra("bookingServicesImage")
        val customerName = intent.getStringExtra("bookingCustomerName") ?: "Customer Name"
        val customerAddress = intent.getStringExtra("bookingCustomerAdd") ?: "Address Not Available"
        val customerNumber = intent.getStringExtra("bookingCustomerMobileNumber") ?: "Phone Not Available"
        val bookingStatus = intent.getStringExtra("bookingStatus") ?: "Status Not Available"

        binding.toolbar.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.toolbar.tvTitle.text = "Order Details"

        binding.tvCustomerName.text = customerName
        binding.tvCustomerAdd.text = customerAddress
        binding.tvCustomerNumber.text = "Phone Number - ${customerNumber}"
        binding.tvServicesName.text = servicesName
        binding.tvServiceQty.text = "Qty - $qty"

        serviceImage?.let {
            Glide.with(binding.root.context)
                .load(it)
                .into(binding.ivServicesImage)
        }
    }
}
