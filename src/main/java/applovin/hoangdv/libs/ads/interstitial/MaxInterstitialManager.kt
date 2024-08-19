package applovin.hoangdv.libs.ads.interstitial

import android.app.Activity
import android.content.Context
import applovin.hoangdv.libs.MaxAds
import applovin.hoangdv.libs.data.shared.MaxAdsLibShared
import applovin.hoangdv.libs.listeners.FullScreenAdsListener
import applovin.hoangdv.libs.water_flow.WaterFlow
import common.hoangdz.lib.utils.ads.GlobalAdState
import common.hoangdz.lib.utils.user.PremiumHolder

class MaxInterstitialManager(
    private val context: Context,
    private val adsShared: MaxAdsLibShared,
    private val premiumHolder: PremiumHolder
) : FullScreenAdsListener() {

    companion object {
        var lastTimeShowAds = 0L
        var showing = false
    }

    private var loading = false

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

    private val isValidAds get() = System.currentTimeMillis() - lastTimeShowAds > adsShared.interstitialGap && MaxAds.readyToLoadAds

    val ready get() = interAd?.isReady == true && adsShared.availableForShowFullscreenADS && !GlobalAdState.showingFullScreenADS


    fun show(activity: Activity, onAdPassed: ((Boolean) -> Unit)) {
        this.onAdPassed = onAdPassed
        if (premiumHolder.isPremium && waterFlow?.canShowAds != true || !isValidAds) {
            onAdPassed.invoke(false)
            return
        }
        if (ready) {
            interAd?.show(activity)
        } else {
            onAdPassed.invoke(false)
            loadAds()
        }
    }

    override fun onAdPassed(hasShow: Boolean) {
        lastTimeShowAds = System.currentTimeMillis()
        onAdPassed?.invoke(hasShow)
        loadAds()
        showing = false
    }

    override fun onAdFailedToLoad() {
        loading = false
        interAd = null
        waterFlow?.failed {
            loadAds()
        }
        lastTimeShowAds = System.currentTimeMillis()
    }

    override fun onAdFailedToShow() {
        interAd = null
        showing = false
    }

    fun loadAds() {
        if (loading || interAd != null) return
        loading = true
        interAd = MaxInterstitial(context, waterFlow?.currentId ?: return, this).also {
            it.loadAd()
        }
    }

    override fun onAdLoaded() {
        loading = false
        waterFlow?.reset()
    }

    override fun onAdShowed() {
        interAd = null
        lastTimeShowAds = System.currentTimeMillis()
        showing = true
    }

}