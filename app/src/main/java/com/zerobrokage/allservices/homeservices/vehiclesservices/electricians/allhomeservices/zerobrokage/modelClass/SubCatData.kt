package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class SubCatData(
    val `data`: List<SubCatItemData>,
    val message: String,
    val success: Boolean
)

data class SubCatItemData(
    val background_image: String,
    val featured: Int,
    val icon: String,
    val id: Int,
    val name: String,
    val slug: String,
    val trending: Int
)