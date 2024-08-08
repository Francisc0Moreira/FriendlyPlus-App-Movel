
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")

}


android {
    namespace = "estg.ipp.pt.friendly"
    compileSdk = 34

    defaultConfig {
        applicationId = "estg.ipp.pt.friendly"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {



    // Google Sign In SDK
    implementation("com.google.android.gms:play-services-auth:20.5.0")


    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")

    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.google.firebase:firebase-storage-ktx")


    //AsyncImage
    implementation("io.coil-kt:coil-compose:2.3.0")


    implementation("androidx.room:room-common:2.6.1")
    //Room
    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    //annotationProcessor("androidx.room:room.compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")


    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.2.1")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.3.1")

    // Para usar as localizações
    implementation("com.google.android.gms:play-services-maps:18.0.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    //Para usar os mapas
    implementation("com.google.maps.android:maps-compose:2.7.3")
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.maps.android:maps-compose-widgets:2.7.3")

    //Para usar nas routes entre screens
    implementation("androidx.navigation:navigation-compose:2.7.5")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.2.1")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.3.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.5")
    implementation("com.google.firebase:protolite-well-known-types:18.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


}