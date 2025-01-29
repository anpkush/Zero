package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage

import android.content.Context

object Dimensions {

    fun fontSizeOverSmall(context: Context): Float {
        return if (context.resources.displayMetrics.widthPixels >= 1300) 10f else 8f
    }

    fun fontSizeExtraSmall(context: Context): Float {
        return if (context.resources.displayMetrics.widthPixels >= 1300) 12f else 10f
    }

    fun fontSizeSmall(context: Context): Float {
        return if (context.resources.displayMetrics.widthPixels >= 1300) 14f else 12f
    }

    fun fontSizeDefault(context: Context): Float {
        return if (context.resources.displayMetrics.widthPixels >= 1300) 16f else 14f
    }

    fun fontSizeLarge(context: Context): Float {
        return if (context.resources.displayMetrics.widthPixels >= 1300) 18f else 16f
    }

    fun fontSizeExtraLarge(context: Context): Float {
        return if (context.resources.displayMetrics.widthPixels >= 1300) 20f else 18f
    }

    fun fontSizeOverLarge(context: Context): Float {
        return if (context.resources.displayMetrics.widthPixels >= 1300) 26f else 24f
    }

    // Static padding sizes
    const val paddingSizeExtraSmall = 5f
    const val paddingSizeSmall = 10f
    const val paddingSizeDefault = 15f
    const val paddingSizeLarge = 20f
    const val paddingSizeExtraLarge = 25f
    const val paddingSizeExtremeLarge = 30f

    // Static radius sizes
    const val radiusSmall = 5f
    const val radiusDefault = 10f
    const val radiusLarge = 15f
    const val radiusExtraLarge = 20f

    // Static constants
    const val webMaxWidth = 1170f
    const val messageInputLength = 250
}



