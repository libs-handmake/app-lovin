package applovin.hoangdv.libs.ads.interstitial

import android.app.Activity
import android.content.Context
import applovin.hoangdv.libs.listeners.FullScreenAdsListener
import applovin.hoangdv.libs.utils.MaxAdState
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import common.hoangdz.lib.extensions.logError
import common.hoangdz.lib.utils.ads.GlobalAdState

class MaxInterstitial(
    private val context: Context,
    private val adID: String,
    private val adListener: FullScreenAdsListener
) : MaxAdListener {

    private val interAd by lazy {
        MaxInterstitialAd(
            adID, context
        ).apply {
            setRevenueListener {
                MaxAdState.onAdPaidEvent?.invoke(it)
            }
        }
    }



    init {
        interAd.setListener(this)
    }

    private var loading = false

    val isReady get() = interAd.isReady

    fun loadAd() {
        if (loading) return
        logError("Max interstitial loadAd")
        loading = true
        interAd.loadAd()
    }

    fun show(activity: Activity) {
        logError("Max interstitial show")
        interAd.showAd(activity)
    }

    override fun onAdLoaded(p0: MaxAd) {
        logError("Max interstitial onAdLoaded")
        loading = false
        adListener.onAdLoaded()
    }

    override fun onAdDisplayed(p0: MaxAd) {
        adListener.onAdShowed()
        GlobalAdState.showingFullScreenADS = true
    }

    override fun onAdHidden(p0: MaxAd) {
        logError("Max interstitial onAdHidden")
        adListener.onAdPassed(true)
        GlobalAdState.showingFullScreenADS = false
        MaxInterstitialManager.lastTimeShowAds = System.currentTimeMillis()
    }


    override fun onAdClicked(p0: MaxAd) {
    }

    override fun onAdLoadFailed(p0: String, p1: MaxError) {
        loading = false
        logError("Max interstitial onAdLoadFailed to load ${p1.message}")
        adListener.onAdFailedToLoad()
    }

    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
        logError("Max interstitial onAdDisplayFailed to load ${p1.message}")
        adListener.onAdFailedToShow()
    }
}