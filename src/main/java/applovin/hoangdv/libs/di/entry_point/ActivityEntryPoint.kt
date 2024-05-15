package applovin.hoangdv.libs.di.entry_point

import applovin.hoangdv.libs.ads.interstitial.MaxInterstitialManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ActivityEntryPoint {
    fun maxInterstitialManager(): MaxInterstitialManager
}