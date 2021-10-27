plugins {
    id("kotlin-android")
    id("com.android.library")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

ext {
    set("PUBLISH_ARTIFACT_ID", "tuna-ui")
}

apply(from = "${rootProject.projectDir}/scripts/publish-module.gradle")

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildToolsVersion

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        vectorDrawables.useSupportLibrary = true
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

repositories {
    maven {
        url = uri("https://cardinalcommerceprod.jfrog.io/artifactory/android")
        credentials {
            username = System.getenv("CYBERSOURCE_USER")
            password = System.getenv("CYBERSOURCE_API_KEY")
        }
    }
}

dependencies {
    implementation(Libraries.stdlib)

    implementation(Libraries.legacySupportV4)
    implementation(Libraries.lifecycleLivedata)
    implementation(Libraries.lifecycleViewModel)
    api(Libraries.lifecycleJava8)

    implementation(Libraries.coreKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.material)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.coroutinesAndroid)
    implementation(Libraries.activity)
    implementation(Libraries.navigationUi)
    implementation(Libraries.navigationFragment)
    implementation(Libraries.shimmer)

    implementation(project(":tuna-android"))
    implementation(project(":tuna-android-kt"))
    implementation(project(":tunacr"))
    api(project(":tunacommons"))

    testImplementation(Libraries.junit)
    testImplementation(Libraries.mockitoKotlin)
    testImplementation(Libraries.mockitoInline)
}
