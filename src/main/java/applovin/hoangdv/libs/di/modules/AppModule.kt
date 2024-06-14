package applovin.hoangdv.libs.di.modules

import android.content.Context
import applovin.hoangdv.libs.MaxAds
import applovin.hoangdv.libs.ads.app_open.MaxAppOpen
import applovin.hoangdv.libs.ads.interstitial.MaxInterstitialManager
import applovin.hoangdv.libs.ads.nativeAds.MaxNativeManager
import applovin.hoangdv.libs.data.remote.AdsRemoteConfigs
import applovin.hoangdv.libs.data.shared.AdsShared_Impl
import applovin.hoangdv.libs.data.shared.MaxAdsLibShared
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAdShared(@ApplicationContext context: Context): MaxAdsLibShared =
        AdsShared_Impl(context)

    @Singleton
    @Provides
    fun provideMaxAds(@ApplicationContext context: Context, adsRemoteConfigs: AdsRemoteConfigs) =
        MaxAds(context, adsRemoteConfigs)

    @Singleton
    @Provides
    fun provideAppOpen(@ApplicationContext context: Context, adsShared: MaxAdsLibShared) =
        MaxAppOpen(context, adsShared)

    @Singleton
    @Provides
    fun provideMaxNativeManager(@ApplicationContext context: Context, adsShared: MaxAdsLibShared) =
        MaxNativeManager(context, adsShared)

    @Singleton
    @Provides
    fun provideAdsRemoteConfigs(adsShared: MaxAdsLibShared) = AdsRemoteConfigs(adsShared)

    @Singleton
    @Provides
    fun provideMaxInterManager(@ApplicationContext context: Context, adsShared: MaxAdsLibShared) =
        MaxInterstitialManager(context, adsShared)

}