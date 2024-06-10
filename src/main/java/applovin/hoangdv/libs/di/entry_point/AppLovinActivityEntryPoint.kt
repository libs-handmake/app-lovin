package applovin.hoangdv.libs.di.entry_point

import applovin.hoangdv.libs.ads.banner.MaxBannerLoader
import applovin.hoangdv.libs.ads.interstitial.MaxInterstitialManager
import applovin.hoangdv.libs.di.qualifier.ActivityBannerAd
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface AppLovinActivityEntryPoint {
    fun maxInterstitialManager(): MaxInterstitialManager

    @ActivityBannerAd
    fun maxBannerLoader(): MaxBannerLoader
}