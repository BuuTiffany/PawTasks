plugins {
    id("com.android.application")
    id("com.amplifyframework.amplifytools")
}

android {
    namespace = "com.example.pawtasks"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.pawtasks"
        minSdk = 24
        targetSdk = 33
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.19")
    implementation("com.amplifyframework:core:2.14.0")
    implementation("com.amplifyframework:aws-api:2.14.0")
    implementation("com.amplifyframework:aws-auth-cognito:2.14.0")
    implementation("com.amplifyframework:aws-datastore:2.14.0")
}