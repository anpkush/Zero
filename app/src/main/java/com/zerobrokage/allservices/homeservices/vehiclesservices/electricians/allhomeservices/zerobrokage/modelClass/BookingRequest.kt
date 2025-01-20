package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class BookingRequest(
    val sub_menu_id: List<Int>,
    val booking_date: String,
    val booking_time: String,
    val qty: Int,
    val full_address: String,
    val name: String,
    val email: String,
    val mobile_number: String
)
data class BookingResponse(
    val success: Boolean,
    val message: String,
    val data: BookingData
)

data class BookingData(
    val bookings: List<CreateBooking>
)

data class CreateBooking(
    val enquiries_id: String,
    val sub_menu_id: Int,
    val service_name: String,
    val service_description: String,
    val service_image: String,
    val service_menu_id: String,
    val booking_date: String,
    val booking_time: String,
    val qty: Int,
    val full_address: String,
    val name: String,
    val email: String,
    val mobile_number: String,
    val status: String,
    val updated_at: String,
    val created_at: String,
    val id: Int
)

