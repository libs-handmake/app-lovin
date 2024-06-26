@file:Suppress("UnstableApiUsage")

import compose_config.composeImplementations
import core_configs.coreAppImplementations
import core_configs.jetpackComponentImplementation
import firebase_configs.firebaseCoreImplementation


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
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = PluginsVer.COMPOSE_COMPILER
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
    coreAppImplementations()
    jetpackComponentImplementation()
    composeImplementations()
    firebaseCoreImplementation()
    implementation(Deps.SHIMMER_LAYOUT)
    implementation(Deps.APP_LOVIN)

//    implementation(AppLovinDeps.ADMOB_ADAPTER)
//    implementation(AppLovinDeps.IRON_SOURCE_ADAPTER)
//    implementation(AppLovinDeps.MINTEGRAL_ADAPTER)
}

kapt {
    correctErrorTypes = true
}
