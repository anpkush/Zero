package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CartViewApi(
    val `data`: List<CartData>,
    val message: String,
    val success: Boolean
): Parcelable

@Parcelize
data class CartData(
        val description: String,
        val id: Int,
        val image: String,
        val name: String,
        var qty: Int,
        val sub_menu_id: Int
):Parcelable

