package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class OtpRequest(
    val mobile_number: String,
    val otp: String
)

data class OtpVerificationResponse(
    val success: Boolean,
    val message: String,
    val id: Int,
    val name: String,
    val countryCode: String,
    val mobileNumber: String
)