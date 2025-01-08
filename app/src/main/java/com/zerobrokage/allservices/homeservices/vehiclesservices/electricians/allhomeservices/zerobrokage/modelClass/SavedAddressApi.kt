package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class SavedAddressApi(
    val addresses: List<Addresse>,
    val message: String
) {
    data class Addresse(
        val address1: Any,
        val address2: Any,
        val address_note: Any,
        val area_colony: Any,
        val building_name: Any,
        val city: String,
        val country: Any,
        val created_at: String,
        val created_by: Any,
        val district: Any,
        val email: String,
        val enquiries_id: Int,
        val house_number: String,
        val id: Int,
        val isDefault: String,
        val landmark: Any,
        val latitude: Any,
        val longitude: Any,
        val mobile_number: String,
        val name: String,
        val pincode: String,
        val road_name: String,
        val state: String,
        val type: String,
        val updated_at: String,
        val updated_by: Any,
        val user_id: Any
    )
}