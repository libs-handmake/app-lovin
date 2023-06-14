package applovin.hoangdv.libs.di.modules

import android.content.Context
import applovin.hoangdv.libs.MaxAds
import applovin.hoangdv.libs.ads.app_open.MaxAppOpen
import applovin.hoangdv.libs.ads.nativeAds.MaxNativeManager
import applovin.hoangdv.libs.data.remote.AdsRemoteConfigs
import applovin.hoangdv.libs.data.shared.AdsShared
import applovin.hoangdv.libs.data.shared.AdsShared_Impl
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
    fun provideAdShared(@ApplicationContext context: Context): AdsShared = AdsShared_Impl(context)

    @Singleton
    @Provides
    fun provideMaxAds(@ApplicationContext context: Context, adsRemoteConfigs: AdsRemoteConfigs) =
        MaxAds(context, adsRemoteConfigs)

    @Singleton
    @Provides
    fun provideAppOpen(@ApplicationContext context: Context, adsShared: AdsShared) =
        MaxAppOpen(context, adsShared)

    @Singleton
    @Provides
    fun provideMaxNativeManager(@ApplicationContext context: Context, adsShared: AdsShared) =
        MaxNativeManager(context, adsShared)

    @Singleton
    @Provides
    fun provideAdsRemoteConfigs(adsShared: AdsShared) = AdsRemoteConfigs(adsShared)

}