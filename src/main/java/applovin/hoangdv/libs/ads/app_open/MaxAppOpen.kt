package applovin.hoangdv.libs.ads.app_open

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import applovin.hoangdv.libs.MaxAds
import applovin.hoangdv.libs.data.shared.MaxAdsLibShared
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAppOpenAd
import common.hoangdz.lib.extensions.logError
import common.hoangdz.lib.utils.ads.GlobalAdState
import common.hoangdz.lib.utils.user.PremiumHolder

class MaxAppOpen(
    private val context: Context,
    private val adsShared: MaxAdsLibShared,
    private val premiumHolder: PremiumHolder
) : LifecycleEventObserver, MaxAdListener {

    companion object {
        var lastTimeShowAds = 0L
        var disableToShow = false
            set(value) {
                logError("Max App open disable to show: $value")
                field = value
            }
        var isAdShowing = false
    }

    private var loading = false

    private var openAd: MaxAppOpenAd? = null

    private val readyForShow get() = System.currentTimeMillis() - lastTimeShowAds > adsShared.appOpenGap && adsShared.availableForShowFullscreenADS && !GlobalAdState.showingFullScreenADS && !disableToShow

    private fun loadAd() {
        logError("app open for max: load AD $loading ${openAd?.isReady}")
        if (loading || openAd?.isReady == true) return
        loading = true
        openAd?.loadAd()
    }

    fun initAppOpen() {
        deinit()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        openAd = MaxAppOpenAd(
            MaxAds.adUnitId?.appOpen ?: return, context
        )
        openAd?.setListener(this)
        loadAd()
    }

    fun deinit() {
        disableToShow = false
        lastTimeShowAds = 0L
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        openAd?.destroy()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_RESUME) {
            disableToShow = false
            showAds()
        }
    }

    private fun showAds() {
        if (premiumHolder.isPremium || isAdShowing || !readyForShow) return
        if (openAd?.isReady == true) {
            openAd?.showAd()
        } else loadAd()
    }

    override fun onAdLoaded(p0: MaxAd) {
        logError("app open for max: onAdLoaded")
        loading = false
    }

    override fun onAdDisplayed(p0: MaxAd) {
        isAdShowing = true
        GlobalAdState.showingFullScreenADS = true
        lastTimeShowAds = System.currentTimeMillis()
    }

    override fun onAdHidden(p0: MaxAd) {
        logError("app open for max: onAdHidden")
        isAdShowing = false
        GlobalAdState.showingFullScreenADS = false
        lastTimeShowAds = System.currentTimeMillis()
        loadAd()
    }

    override fun onAdClicked(p0: MaxAd) {
    }

    override fun onAdLoadFailed(p0: String, p1: MaxError) {
        logError("app open for max: onAdLoadFailed")
        loading = false
    }

    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
        isAdShowing = false
        GlobalAdState.showingFullScreenADS = false
        loadAd()
    }
}