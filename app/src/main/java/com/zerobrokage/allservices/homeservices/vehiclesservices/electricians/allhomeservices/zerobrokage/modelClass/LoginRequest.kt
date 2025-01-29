package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class LoginRequest(
    val country_code: String,
    val mobile_number: String,
    val name: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val otp: String,
    val name: String,
    val country_code: String,
    val mobile_number: String,
    val api_response: String
)