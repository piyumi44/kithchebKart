// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    // Add the Google Services plugin declaration here
    id("com.google.gms.google-services") version "4.4.3" apply false // <<< ADD THIS LINE
    // Note: The version "4.4.2" is common, but always check Firebase documentation
    // for the latest recommended version.
}