package applovin.hoangdv.libs.ads.interstitial

import android.app.Activity
import applovin.hoangdv.libs.MaxAds
import applovin.hoangdv.libs.data.shared.AdsShared
import applovin.hoangdv.libs.listeners.FullScreenAdsListener
import applovin.hoangdv.libs.water_flow.WaterFlow

class MaxInterstitialManager(private val activity: Activity, private val adsShared: AdsShared) :
    FullScreenAdsListener() {

    companion object {
        var lastTimeLoadAds = 0L
        var showing = false
    }

    private var onAdPassed: (() -> Unit)? = null

    private var interAd: MaxInterstitial? = null

    private val waterFlow by lazy {
        MaxAds.adUnitId?.let {
            WaterFlow(
                ids = mutableListOf(
                    it.highFloorInterID,
                    it.mediumFloorInterId,
                    it.allPriceInterAd
                ),
                normalID = it.normalInterAd,
                adsShared = adsShared
            )
        }
    }

    private val isValidAds get() = System.currentTimeMillis() - lastTimeLoadAds > adsShared.interstitialGap

    val ready get() = interAd?.isReady == true && adsShared.availableForShowFullscreenADS


    fun show(onAdPassed: (() -> Unit)) {
        this.onAdPassed = onAdPassed
        if (waterFlow?.canShowAds != true || !isValidAds) {
            onAdPassed.invoke()
            return
        }
        if (ready) {
            interAd?.show()
        } else {
            onAdPassed.invoke()
            loadAds()
        }
    }

    override fun onAdPassed() {
        lastTimeLoadAds = System.currentTimeMillis()
        onAdPassed?.invoke()
        loadAds()
        showing = false
    }

    override fun onAdFailedToLoad() {
        interAd = null
        waterFlow?.failed {
            loadAds()
        }
        lastTimeLoadAds = System.currentTimeMillis()
    }

    override fun onAdFailedToShow() {
        interAd = null
        showing = false
    }

    fun loadAds() {
        if (interAd == null) {
            interAd = MaxInterstitial(activity, waterFlow?.currentId ?: return, this)
        }
        interAd?.loadAd()
    }

    override fun onAdLoaded() {
        waterFlow?.reset()
    }

    override fun onAdShowed() {
        lastTimeLoadAds = System.currentTimeMillis()
        showing = true
    }

}