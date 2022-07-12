plugins {
    id("kotlin-android")
    id("com.android.library")
}

ext {
    set("PUBLISH_ARTIFACT_ID", "tuna-wallets")
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
    implementation(Libraries.coreKtx)

    implementation(project(":tuna-android"))
    implementation(project(":tuna-android-kt"))

    testImplementation(Libraries.junit)
    androidTestImplementation(Libraries.androidxExtJunit)
    androidTestImplementation(Libraries.androidxEspressoCore)
}
