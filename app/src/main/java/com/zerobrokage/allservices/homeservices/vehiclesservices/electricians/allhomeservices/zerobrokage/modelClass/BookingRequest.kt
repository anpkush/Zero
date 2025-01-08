package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class BookingRequest(
    val sub_menu_id: Int,
    val booking_date: String,
    val booking_time: String,
    val qty: Int,
    val full_address: String,
    val name: String,
    val email: String,
    val mobile_number: String
)
data class BookingResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val booking: Booking
    ) {
        data class Booking(
            val booking_date: String,
            val booking_time: String,
            val created_at: String,
            val email: String,
            val enquiries_id: String,
            val full_address: String,
            val id: Int,
            val mobile_number: String,
            val name: String,
            val qty: Int,
            val service_details: ServiceDetails,
            val status: String,
            val sub_menu_id: Int,
            val updated_at: String
        ) {
            data class ServiceDetails(
                val description: String,
                val id: Int,
                val image: String,
                val menu_id: String,
                val name: String
            )
        }
    }
}
