package applovin.hoangdv.libs

import android.content.Context
import applovin.hoangdv.libs.data.remote.AdsRemoteConfigs
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration

class MaxAds(private val context: Context, private val adsRemoteConfigs: AdsRemoteConfigs) {

    companion object {
        var adUnitId: MaxAdUnitId? = null
    }

    fun initial(adUnitId: MaxAdUnitId, onInitial: ((AppLovinSdkConfiguration) -> Unit)? = null) {
        Companion.adUnitId = adUnitId
        adsRemoteConfigs.fetchAdsConfig()
        AppLovinSdk.getInstance(context).apply {
            mediationProvider = "max"
            initializeSdk {
                onInitial?.invoke(it)
            }
            settings.apply {
                setVerboseLogging(true)
                testDeviceAdvertisingIds = mutableListOf(
                    "266c7c33-7c5f-48bd-a671-37781a593b19",
                    "2f7f600f-731c-4bac-a6d5-e8b29fa3b5dd"
                )
            }
        }
    }
}