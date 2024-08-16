plugins {
    alias(libs.plugins.android.library)
    id(libs.plugins.hoangdv.library.get().pluginId)
    id(libs.plugins.hoangdv.core.get().pluginId)
    id(libs.plugins.hoangdv.jetpack.compose.get().pluginId)
    id(libs.plugins.hoangdv.firebase.core.get().pluginId)
}

android {
    namespace = "applovin.hoangdv.libs"
}

dependencies {
    implementation(project(":base:android-common"))
    implementation(libs.shimmer)
    implementation(libs.applovin.sdk)
}
