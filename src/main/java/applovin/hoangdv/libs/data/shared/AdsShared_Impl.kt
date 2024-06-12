package applovin.hoangdv.libs.data.shared

import android.content.Context
import applovin.hoangdv.libs.ads.app_open.MaxAppOpen
import applovin.hoangdv.libs.ads.interstitial.MaxInterstitialManager
import applovin.hoangdv.libs.utils.MaxAdState
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlin.math.max

@Suppress("ClassName")
class AdsShared_Impl(private val context: Context) : MaxAdsLibShared {

    companion object {
        const val INTER_GAP = "applovin_inter_ads_gap"

        const val APP_OPEN_GAP = "applovin_app_open_gap"

        const val FULL_SCREEN_GAP = "applovin_full_screen_gap"

        const val MIN_WATER_FLOW_GAP = "applovin_min_water_flow_gap"

        const val MAX_WATER_FLOW_GAP = "applovin_max_water_flow_gap"

        const val NUMBER_NATIVE_NEED_TO_LOAD = "applovin_number_native_ad_need_load"

        const val IS_MONETIZATION = "applovin_is_monetization"
    }

    private val shared by lazy {
        context.getSharedPreferences(
            "applovin.hoangdv.libs.max_ads_configs", Context.MODE_PRIVATE
        )
    }

    private val edit by lazy { shared.edit() }

    override val firebaseDefaultRemoteConfig: Map<String, Any>
        get() = mapOf(
            INTER_GAP to shared.getLong(INTER_GAP, 30_000),
            APP_OPEN_GAP to appOpenGap,
            FULL_SCREEN_GAP to fullScreenAdsGap,
            MIN_WATER_FLOW_GAP to minWaterFlowGap,
            MAX_WATER_FLOW_GAP to maxWaterFlowGap,
            NUMBER_NATIVE_NEED_TO_LOAD to numberNativeAdsNeedToLoad,
            IS_MONETIZATION to isMonetization
        )

    override var interstitialGap: Long
        get() {
            return MaxAdState.overrideInterGap ?: shared.getLong(INTER_GAP, 30_000)
        }
        set(value) {
            edit.putLong(INTER_GAP, value).apply()
        }

    override var appOpenGap: Long
        get() = 0L//shared.getLong(APP_OPEN_GAP, 15_000L)
        set(value) {
            edit.putLong(APP_OPEN_GAP, value).apply()
        }

    override var fullScreenAdsGap: Long
        get() = 0L//shared.getLong(FULL_SCREEN_GAP, 5000)
        set(value) {
            edit.putLong(FULL_SCREEN_GAP, value).apply()
        }

    override var minWaterFlowGap: Long
        get() = shared.getLong(MIN_WATER_FLOW_GAP, 5000)
        set(value) {
            shared.getLong(MIN_WATER_FLOW_GAP, value)
        }

    override var maxWaterFlowGap: Long
        get() = shared.getLong(MAX_WATER_FLOW_GAP, 30_000)
        set(value) {
            edit.putLong(MAX_WATER_FLOW_GAP, value).apply()
        }

    override var numberNativeAdsNeedToLoad: Long
        get() = max(shared.getLong(NUMBER_NATIVE_NEED_TO_LOAD, 5), 1)
        set(value) {
            edit.putLong(NUMBER_NATIVE_NEED_TO_LOAD, value).apply()
        }
    override var isMonetization: Boolean
        get() = shared.getBoolean(IS_MONETIZATION, false)
        set(value) {
            edit.putBoolean(IS_MONETIZATION, value).apply()
        }
    override val availableForShowFullscreenADS: Boolean
        get() = System.currentTimeMillis() - MaxInterstitialManager.lastTimeShowAds > fullScreenAdsGap && System.currentTimeMillis() - MaxAppOpen.lastTimeShowAds > fullScreenAdsGap && !MaxInterstitialManager.showing && !MaxAppOpen.isAdShowing

    override fun applyFirebaseConfigs(firebaseRemoteConfig: FirebaseRemoteConfig) {
        interstitialGap = firebaseRemoteConfig.getLong(INTER_GAP)
        appOpenGap = firebaseRemoteConfig.getLong(APP_OPEN_GAP)
        fullScreenAdsGap = firebaseRemoteConfig.getLong(FULL_SCREEN_GAP)
        minWaterFlowGap = firebaseRemoteConfig.getLong(MIN_WATER_FLOW_GAP)
        maxWaterFlowGap = firebaseRemoteConfig.getLong(MAX_WATER_FLOW_GAP)
        numberNativeAdsNeedToLoad = firebaseRemoteConfig.getLong(NUMBER_NATIVE_NEED_TO_LOAD)
        isMonetization = firebaseRemoteConfig.getBoolean(IS_MONETIZATION)
    }
}