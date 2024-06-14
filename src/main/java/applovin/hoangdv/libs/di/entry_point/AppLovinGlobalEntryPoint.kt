package applovin.hoangdv.libs.di.entry_point

import applovin.hoangdv.libs.ads.interstitial.MaxInterstitialManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppLovinGlobalEntryPoint {
    fun maxInterManager(): MaxInterstitialManager
}