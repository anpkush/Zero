package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass

data class FAQsData(
    val faqs_data: List<FaqsData>,
    val message: String,
    val success: Boolean
) {
    data class FaqsData(
        val answer: String,
        val id: Int,
        val question: String
    )
}