plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services") version "4.4.1"

}

android {
    namespace = "com.pgs.lie.detector.prank.test.fingerprint.scanner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pgs.lie.detector.prank.test.fingerprint.scanner"
        minSdk = 24
        targetSdk = 34
        versionCode = 9
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
val lifecycle_version = "2.6.2"
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.tbuonomo:dotsindicator:4.2")


    implementation("androidx.camera:camera-core:1.2.2")
    implementation("androidx.camera:camera-camera2:1.2.2")
    implementation("androidx.camera:camera-extensions:1.2.2")
    implementation("com.github.bumptech.glide:glide:4.12.0")



    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.intuit.ssp:ssp-android:1.1.0")

        //gif
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.17")

    //glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //anim
    implementation ("com.airbnb.android:lottie:6.4.0")

    //in appreview
    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.gms:google-services:4.3.3")
    implementation ("com.google.firebase:firebase-analytics-ktx:17.6.0")

    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-analytics")

    //admod
    implementation("com.google.android.gms:play-services-ads:23.0.0")

    //appopen

    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")

    //adjust
//    implementation ("com.adjust.sdk:adjust-android:4.33.5")
//    implementation ("com.android.installreferrer:installreferrer:2.2")
//    // Add the following if you are using the Adjust SDK inside web views on your app
//    implementation ("com.adjust.sdk:adjust-android-webbridge:4.33.5")

    implementation ("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation ("com.google.android.gms:play-services-appset:16.0.2")

    implementation(fileTree("libs") {
        include("*.aar")
    })

    implementation(files("libs/adjust-android-signature-3.12.0.aar"))

    implementation(files("libs/adjust-android-4.35.0.aar"))


}