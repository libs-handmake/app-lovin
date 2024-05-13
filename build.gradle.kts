@file:Suppress("UnstableApiUsage")

plugins {
    id("kotlin-kapt")
    id(Plugins.ANDROID_LIBS)
    id(Plugins.HILT)
    kotlin("android")
}

android {
    namespace = "applovin.hoangdv.libs"
    compileSdk = Configs.TARGET_SDK

    defaultConfig {
        minSdk = Configs.MIN_SUPPORTED_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = Configs.JAVA_TARGET
        targetCompatibility = Configs.JAVA_TARGET
    }
    kotlinOptions {
        jvmTarget = Configs.JVM_TARGET
    }
}

dependencies {
    implementation(project(":base:android-common"))
    implementation(Deps.KTX_CORE)
    implementation(Deps.APPCOMPAT)
    implementation(Deps.MATERIAL)
    testImplementation(Deps.JUNIT_TEST)
    androidTestImplementation(Deps.JUNIT_TEST_EXT)
    androidTestImplementation(Deps.EPRESSO_CORE)

    implementation(Deps.APP_LOVIN)
    implementation(Deps.HILT)
    kapt(Deps.HILT_COMPILER)
    implementation(Deps.LIFECYCLE_PROCESS)
    implementation(Deps.SDP)
    implementation(Deps.SSP)
    implementation(Deps.SHIMMER_LAYOUT)
    implementation(platform(Deps.FIREBASE_BOM))
    implementation(Deps.FIREBASE_REMOTE_CONFIG)

    implementation(Deps.RECYCLER_VIEW)
//    implementation(AppLovinDeps.ADMOB_ADAPTER)
//    implementation(AppLovinDeps.IRON_SOURCE_ADAPTER)
//    implementation(AppLovinDeps.MINTEGRAL_ADAPTER)
}