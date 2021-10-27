plugins {
    id("kotlin-android")
    id("com.android.library")
}

ext {
    set("PUBLISH_ARTIFACT_ID", "tuna-android")
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
    implementation(Libraries.coroutinesAndroid)

    // Cardinal Mobile SDK
    implementation(Libraries.cardinalMobileSDK)

    api(project(":tuna"))

    testImplementation(Libraries.junit)
    androidTestImplementation(Libraries.androidxExtJunit)
    androidTestImplementation(Libraries.androidxEspressoCore)
}
