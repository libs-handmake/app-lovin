package applovin.hoangdv.libs.listeners

abstract class FullScreenAdsListener {
    open fun onAdPassed(hasShow: Boolean) {}

    open fun onAdFailedToLoad() {}

    open fun onAdReward() {}

    open fun onAdShowed() {}

    open fun onAdFailedToShow() {}

    open fun onAdLoaded() {}

    open fun onAdHidden() {}
}