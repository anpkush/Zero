package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class CartViewApi(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val description: String,
        val id: Int,
        val image: String,
        val name: String,
        var qty: Int,
        val sub_menu_id: Int
    )
}
