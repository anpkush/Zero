package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class DeviceIdRequest(
    val device_id: String
)
data class DeviceIdResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val device_id: String
    )
}
