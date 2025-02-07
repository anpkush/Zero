package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class OtpRequest(
    val mobile_number: String,
    val otp: String,
    val name : String,
    val country_code: String

)

data class OtpVerificationResponse(
    val success: Boolean,
    val message: String,
    val id: Int,
    val name: String,
    val country_code: String,
    val mobile_number: String
)