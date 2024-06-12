package applovin.hoangdv.libs.ads.new_native_ads.loader

import android.content.Context
import applovin.hoangdv.libs.utils.MaxAdState
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView

data class NativeAdKeeper(
    private val context: Context,
    private val adId: String,
    private val onFailedToLoad: () -> Unit = {},
    private val onLoaded: (NativeAdKeeper) -> Unit = {}
) {


    private var isDestroy = false

    var loaderID: String? = null

    var nativeAd: MaxAd? = null

    val adLoader by lazy {
        MaxNativeAdLoader(
            adId, context
        ).apply {
            setNativeAdListener(object : MaxNativeAdListener() {
                override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd) {
                    nativeAd = p1
                    onLoaded(this@NativeAdKeeper)
                }

                override fun onNativeAdExpired(p0: MaxAd) {
                    super.onNativeAdExpired(p0)
                    onFailedToLoad()
                }

                override fun onNativeAdLoadFailed(p0: String, p1: MaxError) {
                    onFailedToLoad()
                }
            })

            setRevenueListener {
                MaxAdState.onAdPaidEvent?.invoke(it)
            }
        }
    }


    fun render(adView: MaxNativeAdView) {
        adLoader.render(adView, nativeAd)
    }


    fun destroy() {
        adLoader.destroy()
        isDestroy = true
    }

    fun loadAd() {
        adLoader.loadAd()
    }


}