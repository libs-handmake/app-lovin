package applovin.hoangdv.libs.ads.interstitial

import android.app.Activity
import applovin.hoangdv.libs.MaxAds
import applovin.hoangdv.libs.data.shared.AdsShared
import applovin.hoangdv.libs.listeners.FullScreenAdsListener
import applovin.hoangdv.libs.water_flow.WaterFlow
import common.hoangdz.lib.utils.ads.GlobalAdState

class MaxInterstitialManager(private val activity: Activity, private val adsShared: AdsShared) :
    FullScreenAdsListener() {

    companion object {
        var lastTimeLoadAds = 0L
        var showing = false
    }

    private var onAdPassed: ((Boolean) -> Unit)? = null

    private var interAd: MaxInterstitial? = null

    private val waterFlow by lazy {
        MaxAds.adUnitId?.let {
            WaterFlow(
                ids = mutableListOf(
                    it.highFloorInterID, it.mediumFloorInterId, it.allPriceInterAd
                ), normalID = it.normalInterAd, adsShared = adsShared
            )
        }
    }

    private val isValidAds get() = System.currentTimeMillis() - lastTimeLoadAds > adsShared.interstitialGap && MaxAds.readyToLoadAds

    val ready get() = interAd?.isReady == true && adsShared.availableForShowFullscreenADS && !GlobalAdState.showingFullScreenADS


    fun show(onAdPassed: ((Boolean) -> Unit)) {
        this.onAdPassed = onAdPassed
        if (waterFlow?.canShowAds != true || !isValidAds) {
            onAdPassed.invoke(false)
            return
        }
        if (ready) {
            interAd?.show()
        } else {
            onAdPassed.invoke(false)
            loadAds()
        }
    }

    override fun onAdPassed(hasShow: Boolean) {
        lastTimeLoadAds = System.currentTimeMillis()
        onAdPassed?.invoke(hasShow)
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