package applovin.hoangdv.libs.ads.interstitial

import android.app.Activity
import applovin.hoangdv.libs.listeners.FullScreenAdsListener
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import common.hoangdz.lib.utils.ads.GlobalAdState

class MaxInterstitial(
    private val activity: Activity,
    private val adID: String,
    private val adListener: FullScreenAdsListener
) : MaxAdListener {

    private val interAd by lazy {
        MaxInterstitialAd(
            adID, activity
        )
    }

    init {
        interAd.setListener(this)
    }

    private var loading = false

    val isReady get() = interAd.isReady

    fun loadAd() {
        if (loading) return
        loading = true
        interAd.loadAd()
    }

    fun show() {
        interAd.showAd()
    }

    override fun onAdLoaded(p0: MaxAd) {
        loading = false
        adListener.onAdLoaded()
    }

    override fun onAdDisplayed(p0: MaxAd) {
        adListener.onAdShowed()
        GlobalAdState.showingFullScreenADS = true
    }

    override fun onAdHidden(p0: MaxAd) {
        adListener.onAdPassed(true)
        GlobalAdState.showingFullScreenADS = false
        MaxInterstitialManager.lastTimeLoadAds = System.currentTimeMillis()
    }

    override fun onAdClicked(p0: MaxAd) {
    }

    override fun onAdLoadFailed(p0: String, p1: MaxError) {
        loading = false
        adListener.onAdFailedToLoad()
    }

    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
        adListener.onAdFailedToShow()
    }
}