plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id ("kotlin-android")
    id ("kotlin-android-extensions")
}

android {
    namespace = "com.example.vibeverse"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vibeverse"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Material Design
    implementation ("com.google.android.material:material:1.12.0-alpha02")

    // Architectural Components
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Lifecycle
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Coroutine Lifecycle Scopes
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Navigation Component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.6")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    kapt ("com.github.bumptech.glide:ksp:4.14.2")

    // Activity KTX for viewModels()
    implementation ("androidx.activity:activity-ktx:1.8.2")

    //Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.48")
    kapt ("com.google.dagger:hilt-android-compiler:2.48")
//    implementation ("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
//    kapt ("androidx.hilt:hilt-compiler:1.2.0-alpha01")

    // Timber
    implementation ("com.jakewharton.timber:timber:4.7.1")

    // Firebase Firestore
    implementation ("com.google.firebase:firebase-firestore:24.10.0")

    // Firebase Storage KTX
    implementation ("com.google.firebase:firebase-storage-ktx:20.3.0")

    // Firebase Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")

    // ExoPlayer

    api ("com.google.android.exoplayer:exoplayer-core:2.19.1")
    api ("com.google.android.exoplayer:exoplayer-ui:2.19.1")
    api ("com.google.android.exoplayer:extension-mediasession:2.19.1")

//    implementation ("androidx.media3:media3-session:1.2.0")
//
//    implementation ("androidx.media3:media3-exoplayer:1.2.0")
//    implementation ("androidx.media3:media3-exoplayer-dash:1.2.0")
//    implementation ("androidx.media3:media3-ui:1.2.0")

}