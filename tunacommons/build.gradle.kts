plugins {
    id("kotlin-android")
    id("com.android.library")
}

ext {
    set("PUBLISH_VERSION", "0.0.1")
    set("PUBLISH_ARTIFACT_ID", "tuna-commons")
}

apply(from = "${rootProject.projectDir}/scripts/publish-module.gradle")

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildToolsVersion

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(Libraries.stdlib)
    implementation(Libraries.coreKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.material)

    testImplementation(Libraries.junit)
    androidTestImplementation(Libraries.androidxExtJunit)
    androidTestImplementation(Libraries.androidxEspressoCore)
}