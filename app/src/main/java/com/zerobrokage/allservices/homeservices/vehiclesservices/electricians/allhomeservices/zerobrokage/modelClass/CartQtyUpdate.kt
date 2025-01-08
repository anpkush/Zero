package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class CartQtyUpdate(
    val adjustment: Int
)

data class CartViewApi(
    val success: Boolean,
    val data: Data,
    val message: String
) {
    data class Data(
        val cart_item: CartItem,
        val service: Service
    )

    data class CartItem(
        val id: Int,
        val enquiries_id: Int,
        val sub_menu_id: Int,
        val qty: Int,
        val created_at: String,
        val updated_at: String,
        val name: String,
        val image: String,
        val description: String
    )

    data class Service(
        val name: String,
        val image: String,
        val description: String
    )
}
