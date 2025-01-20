package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class BookingListApi(
    val success: Boolean,
    val message: String,
    val `data`: Data
)
data class Data(
    val bookings: List<Booking>
)
data class Booking(
    val id: Int,
    val enquiries_id: Int,
    val sub_menu_id: List<Int>,
    val service_name: String,
    val service_description: String,
    val service_image: String,
    val service_menu_id: Int,
    val booking_date: String,
    val booking_time: String,
    val qty: Int,
    val full_address: String,
    val name: String,
    val email: String,
    val mobile_number: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val service_details: ServiceDetails
)
data class ServiceDetails(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val menu_id: String
)


