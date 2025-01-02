package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        val config = overrideConfiguration ?: Configuration()
        config.fontScale = 1.0f
        super.applyOverrideConfiguration(config)
    }
}
