package applovin.hoangdv.libs.ads.reward

import android.app.Activity
import applovin.hoangdv.libs.MaxAds
import applovin.hoangdv.libs.listeners.FullScreenAdsListener
import applovin.hoangdv.libs.utils.MaxAdState
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd
import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaxRewardAdLoader @Inject constructor() : MaxRewardedAdListener {

    private var showOnLoaded = false

    private var loading = false

    private var rewardAd: MaxRewardedAd? = null

    private var fullScreenAdsListener: FullScreenAdsListener? = null

    private var activityPhantom: PhantomReference<Activity>? = null

    fun show(
        activity: Activity,
        fullScreenAdsListener: FullScreenAdsListener?,
    ) {
        this.activityPhantom = PhantomReference(activity, ReferenceQueue())
        this.fullScreenAdsListener = fullScreenAdsListener
        this.showOnLoaded = true
        if (rewardAd?.isReady == true) {
            rewardAd?.showAd(activity)
        } else loadAd(activity)
    }

    private fun loadAd(activity: Activity) {
        if (loading || rewardAd?.isReady == true) return
        rewardAd = MaxRewardedAd.getInstance(MaxAds.adUnitId?.rewardId ?: return, activity)?.apply {
            setRevenueListener {
                MaxAdState.onAdPaidEvent?.invoke(it)
            }
        }
        loading = true
        rewardAd?.loadAd()
    }

    fun quit() {
        fullScreenAdsListener = null
        showOnLoaded = false
    }

    override fun onAdLoaded(p0: MaxAd) {
        fullScreenAdsListener?.onAdLoaded()
        loading = false
        if (showOnLoaded) show(activityPhantom?.get() ?: return, fullScreenAdsListener)
    }

    override fun onAdDisplayed(p0: MaxAd) {
        fullScreenAdsListener?.onAdShowed()
    }

    override fun onAdHidden(p0: MaxAd) {
        fullScreenAdsListener?.onAdHidden()
    }

    override fun onAdClicked(p0: MaxAd) {
    }

    override fun onAdLoadFailed(p0: String, p1: MaxError) {
        fullScreenAdsListener?.onAdFailedToLoad()
    }

    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
        fullScreenAdsListener?.onAdFailedToShow()
    }

    override fun onUserRewarded(p0: MaxAd, p1: MaxReward) {
        fullScreenAdsListener?.onAdReward()
    }

}