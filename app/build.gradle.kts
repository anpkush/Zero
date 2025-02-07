plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage"
        minSdk = 24
        targetSdk = 34
        versionCode = 11
        versionName = "2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    bundle {
        language {
            enableSplit = false
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Multidex
    implementation("androidx.multidex:multidex:2.0.1")

    // Image Slider
    implementation(libs.imageslideshow)

    // Retrofit
    implementation(libs.retrofit)
    // Gson converter
    implementation(libs.converter.gson)

    // Glide for image loading
    implementation(libs.glide)

    // Location services
    implementation(libs.play.services.location)

    // OTP field
    implementation(libs.pinview)

    // Country code picker
    implementation(libs.ccp.v261)

    // Logging interceptor
    implementation(libs.logging.interceptor)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit.junit)


}
