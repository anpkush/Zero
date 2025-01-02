plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage"
        minSdk = 24
        targetSdk = 34
        versionCode = 6
        versionName = "1.6"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
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

    // Play services location (for version management, check if needed)
    implementation(libs.play.services.location.v2101)
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.3")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    implementation("androidx.test:monitor:1.7.2")
    androidTestImplementation("junit:junit:4.12")

    //Firebase

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-perf")
    implementation("com.google.firebase:firebase-perf:21.0.3")
}

