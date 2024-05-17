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
import common.hoangdz.lib.utils.ads.GlobalAdState

class MaxAppOpen(private val context: Context, private val adsShared: MaxAdsLibShared) :
    LifecycleEventObserver, MaxAdListener {

    companion object {
        var lastTimeShowAds = 0L
        var isAdShowing = false
    }

    private var loading = false

    private val openAd by lazy {
        MaxAppOpenAd(
            MaxAds.adUnitId?.appOpen ?: return@lazy null, context
        )
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private val readyForShow get() = System.currentTimeMillis() - lastTimeShowAds > adsShared.appOpenGap && adsShared.availableForShowFullscreenADS && !GlobalAdState.showingFullScreenADS

    private fun loadAd() {
        if (loading || openAd?.isReady == true) return
        loading = true
        openAd?.loadAd()
    }

    fun initAppOpen() {
        openAd?.setListener(this)
        loadAd()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_RESUME) {
            if (openAd?.isReady == true) showAds()
        }
    }

    private fun showAds() {
        if (isAdShowing || !readyForShow) return
        if (openAd?.isReady == true) {
            openAd?.showAd()
        } else loadAd()
    }

    override fun onAdLoaded(p0: MaxAd) {
        loading = false
    }

    override fun onAdDisplayed(p0: MaxAd) {
        isAdShowing = true
        GlobalAdState.showingFullScreenADS = true
        lastTimeShowAds = System.currentTimeMillis()
    }

    override fun onAdHidden(p0: MaxAd) {
        isAdShowing = false
        GlobalAdState.showingFullScreenADS = false
        lastTimeShowAds = System.currentTimeMillis()
        loadAd()
    }

    override fun onAdClicked(p0: MaxAd) {
    }

    override fun onAdLoadFailed(p0: String, p1: MaxError) {
        loading = false
    }

    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
        isAdShowing = false
        loadAd()
    }
}