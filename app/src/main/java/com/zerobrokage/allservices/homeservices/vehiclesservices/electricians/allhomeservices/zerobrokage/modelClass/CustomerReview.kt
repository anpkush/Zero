package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class CustomerReview(
    val `data`: List<CustomerReviewData>,
    val message: String,
    val success: Boolean
)

data class CustomerReviewData(
    val description: String,
    val id: Int,
    val name: String,
    val profession: String
)