plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.foodproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.foodproject"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.google.firebase:firebase-database:20.3.1")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation ("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("de.hdodenhof:circleimageview:3.1.0")



}