package applovin.hoangdv.libs.listeners

abstract class FullScreenAdsListener {
     open fun onAdPassed(){}

     open fun onAdFailedToLoad(){}

     open fun onAdReward(){}

     open fun onAdShowed(){}

     open fun onAdFailedToShow(){}

     open fun onAdLoaded(){}
}