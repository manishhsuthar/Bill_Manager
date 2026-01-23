plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // ✅ ADD THIS
}

android {
    namespace = "com.example.billmanager"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.billmanager"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/NOTICE.txt"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

dependencies {

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.4")
    implementation("com.google.android.material:material:1.12.0")
    // Firebase Auth (Google Sign-In)
    implementation("com.google.firebase:firebase-auth:23.0.0")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // Google Drive API (REQUIRED)
    implementation("com.google.api-client:google-api-client-android:1.34.0")
    implementation("com.google.http-client:google-http-client-gson:1.43.3")
    implementation("com.google.apis:google-api-services-drive:v3-rev20230822-2.0.0")
}

