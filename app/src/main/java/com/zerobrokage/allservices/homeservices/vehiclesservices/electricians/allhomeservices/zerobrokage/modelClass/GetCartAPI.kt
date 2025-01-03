package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class GetCartAPI(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val description: String,
        val id: Int,
        val image: String,
        val name: String,
        val qty: Int,
        val sub_menu_id: Int
    )
}