package applovin.hoangdv.libs.data.shared

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

interface AdsShared {

    val firebaseDefaultRemoteConfig: Map<String, Any>

    var interstitialGap: Long

    var appOpenGap: Long

    var fullScreenAdsGap: Long

    var minWaterFlowGap: Long

    var maxWaterFlowGap: Long

    var numberNativeAdsNeedToLoad: Long

    var isMonetization: Boolean

    val availableForShowFullscreenADS: Boolean

    fun applyFirebaseConfigs(firebaseRemoteConfig: FirebaseRemoteConfig)

}