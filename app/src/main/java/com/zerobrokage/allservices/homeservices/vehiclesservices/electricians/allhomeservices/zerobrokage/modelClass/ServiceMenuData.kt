package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class ServiceMenuData(
    val `data`: List<ServiceData>,
    val message: String,
    val success: Boolean
)

data class ServiceData(
    val icon: String,
    val id: Int,
    val image: String,
    val name: String,
    val subcategory_id: Int
)
