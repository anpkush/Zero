package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class SavedAddressesApi(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val city: String,
        val email: String,
        val house_number: String,
        val mobile_number: String,
        val name: String,
        val pincode: String,
        val road_name: String,
        val state: String,
        val isDefault: Boolean = false,
        val type: String
    )
}