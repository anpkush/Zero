package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class ItemUpdateRequest(
    val adjustment: String
)

data class ItemUpdateResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val cart_item: CartItem,
        val service: Service
    ) {
        data class CartItem(
            val created_at: String,
            val description: String,
            val enquiries_id: Int,
            val id: Int,
            val image: String,
            val name: String,
            val qty: Int,
            val sub_menu_id: Int,
            val updated_at: String
        )

        data class Service(
            val description: String,
            val image: String,
            val name: String
        )
    }
}