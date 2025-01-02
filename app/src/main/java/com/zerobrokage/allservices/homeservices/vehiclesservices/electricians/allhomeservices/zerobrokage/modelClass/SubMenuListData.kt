package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class SubMenuListData(
    val message: String,
    val submenus_data: List<SubmenusData>,
    val success: Boolean
)

data class SubmenusData(
    val city: String,
    val description: String,
    val details: String,
    val discounted_price: String,
    val id: Int,
    val image: String,
    val menu_id: String,
    val name: String,
    val total_price: String
)
