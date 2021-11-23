plugins {
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.android.application")
    id("kotlin-android-extensions")
}

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildToolsVersion

    defaultConfig {
        applicationId = Android.applicationId
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = Android.versionCode
        versionName = Android.versionName
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = Options.jvmTarget
    }
    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(Libraries.stdlib)
    implementation(Libraries.coreKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.material)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.coroutinesAndroid)
    implementation(Libraries.activity)

    implementation(project(":tuna-android"))
    implementation(project(":tuna-android-kt"))

    implementation(project(":tunaui"))
    implementation(project(":tunacr"))

    implementation(project(":tuna-wallets"))
    implementation(project(":tuna-google-pay"))

    testImplementation(Libraries.junit)
    androidTestImplementation(Libraries.androidxExtJunit)
    androidTestImplementation(Libraries.androidxEspressoCore)
}