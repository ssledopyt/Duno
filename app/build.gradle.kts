import java.util.Properties
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.example.duno"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.duno"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["YANDEX_CLIENT_ID"] = "2b6c4070e97b48b89e0bb53cced0f8f7"
        //buildConfigField("String", "YAMAP", "\"${mapkitApiKey}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //load the values from .properties file
        val keystoreFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        //return empty key in case something goes wrong
        val apiKey = properties.getProperty("YAMAp") ?: ""

        buildConfigField(
            type = "String",
            name = "YAMAp",
            value = apiKey
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        /*release {
            val p = Properties()
            p.load(project.rootProject.file("local.properties").reader())
            val yourKey: String = p.getProperty("YAMAp")
            buildConfigField("String", "YAMAP", "\"$yourKey\"")
        }*/
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.9"
    }

}



dependencies {

    val composeBom = platform("androidx.compose:compose-bom:2024.02.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.ui:ui-viewbinding:1.6.0")
    implementation("androidx.fragment:fragment-ktx:1.7.0-alpha09")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.chargemap.compose:numberpicker:1.0.3")
    implementation ("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")


    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.dagger:hilt-android:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-android-compiler:2.50")
    implementation("com.yandex.android:authsdk:3.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")
    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.0-alpha05")

    implementation("com.yandex.android:maps.mobile:4.5.1-lite")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    testImplementation("junit:junit:4.13.2")


    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}