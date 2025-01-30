package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class CartItemUpdate(
    val adjustment : Int
)

data class CartItemResponse(
    val success : Boolean,
    val message : String
)
