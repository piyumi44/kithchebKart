// app/build.gradle.kts (Module: app)

plugins {
    id("com.android.application") // Using direct ID string
    id("com.google.gms.google-services")
}

android {
    namespace = "com.PROJECT.kitchenkart"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.PROJECT.kitchenkart"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // AndroidX UI Components (Using direct versions - ensures these are found)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Firebase: Use the Firebase BOM for consistent version management
    // *** IMPORTANT: CHECK FIREBASE DOCS FOR THE ABSOLUTE LATEST STABLE BOM VERSION ***
    // (e.g., "33.0.0" is common as of mid-2024, but verify at https://firebase.google.com/docs/android/setup#available-libraries)
    implementation(platform("com.google.firebase:firebase-bom:33.0.0")) // <-- UPDATE THIS VERSION IF NEWER

    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage") // For profile picture uploads

    // Google Identity/Credentials - Use AndroidX Credential Manager (Modern Approach)
    // Check for the latest stable versions here:
    // https://developer.android.com/reference/androidx/credentials/credentials/Overview
    // https://developer.android.com/reference/androidx/credentials/credentials-play-services-auth/Overview
    implementation("androidx.credentials:credentials:1.2.0") // Current stable might be 1.0.0, but using 1.2.0 here as it's common. Verify latest stable.
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0") // Current stable might be 1.0.0, but using 1.2.0 here as it's common. Verify latest stable.

    // This is still needed for general Google Sign-In features that might not be covered
    // directly by the new Credential Manager (e.g., GoogleSignInClient setup).
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // The following were likely causing the "not found" error and are replaced by AndroidX Credential Manager:
    // implementation("com.google.android.libraries.identity.credentials:credentials:1.2.0") // REMOVED
    // implementation("com.google.android.gms:play-services-auth-api-phone:18.0.2") // Often replaced by credentials-play-services-auth
    // implementation(libs.play.services.tasks) // Usually implicitly pulled in by other play-services libs

    // Testing Libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    // Room components
    val room_version = "2.6.1" // Use the latest stable version
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
// To use Kotlin annotation processing tool (kapt) for Kotlin projects, if you convert to Kotlin:
// kapt("androidx.room:room-compiler:$room_version")
}