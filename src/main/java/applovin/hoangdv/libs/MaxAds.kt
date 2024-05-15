package applovin.hoangdv.libs

import android.content.Context
import applovin.hoangdv.libs.data.remote.AdsRemoteConfigs
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import common.hoangdz.lib.extensions.logError

class MaxAds(private val context: Context, private val adsRemoteConfigs: AdsRemoteConfigs) {

    companion object {
        var adUnitId: MaxAdUnitId? = null

        var readyToLoadAds = false
    }

    fun initial(adUnitId: MaxAdUnitId, onInitial: ((AppLovinSdkConfiguration) -> Unit)? = null) {
        Companion.adUnitId = adUnitId
        adsRemoteConfigs.fetchAdsConfig()
        val initConfig = AppLovinSdkInitializationConfiguration.builder(
            adUnitId.apiKey, context
        ).setMediationProvider(AppLovinMediationProvider.MAX).configureSettings {
            it.setVerboseLogging(true)
            it.testDeviceAdvertisingIds = mutableListOf(
                "266c7c33-7c5f-48bd-a671-37781a593b19", "2f7f600f-731c-4bac-a6d5-e8b29fa3b5dd"
            )
        }.build()

        logError("MAX ADS start inititial")
        AppLovinSdk.getInstance(context).apply {
            initialize(initConfig) { sdkConfig ->
                // Start loading ads
                onInitial?.invoke(sdkConfig)
                logError("MAX ADS initialized")
                readyToLoadAds = true
            }
        }

//        logError("Start initial Max SDK")
//        AppLovinSdk.getInstance(context).apply {
//            mediationProvider = "max"
//            initializeSdk {
//                logError("MAX SDK intial successfully")
//                onInitial?.invoke(it)
//                readyToLoadAds = true
//                settings?.apply {
//                    setVerboseLogging(true)
//                    testDeviceAdvertisingIds = mutableListOf(
//                        "266c7c33-7c5f-48bd-a671-37781a593b19",
//                        "2f7f600f-731c-4bac-a6d5-e8b29fa3b5dd"
//                    )
//                }
//            }
//
//        }
    }
}