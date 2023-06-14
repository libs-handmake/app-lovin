package applovin.hoangdv.libs.di.modules

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import applovin.hoangdv.libs.ads.banner.MaxBannerLoader
import applovin.hoangdv.libs.ads.interstitial.MaxInterstitialManager
import applovin.hoangdv.libs.data.shared.AdsShared
import applovin.hoangdv.libs.di.qualifier.ActivityBannerAd
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class ActivityModule {

    @Provides
    @ActivityScoped
    fun provideInterstitialAds(activity: Activity, adsShared: AdsShared) =
        MaxInterstitialManager(activity, adsShared)

    @ActivityBannerAd
    @Provides
    fun provideBannerAd(@ActivityContext context: Context, activity: Activity) =
        MaxBannerLoader(context, (activity as AppCompatActivity))

}