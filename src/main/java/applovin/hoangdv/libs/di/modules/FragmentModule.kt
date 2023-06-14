package applovin.hoangdv.libs.di.modules

import android.content.Context
import androidx.fragment.app.Fragment
import applovin.hoangdv.libs.ads.banner.MaxBannerLoader
import applovin.hoangdv.libs.di.qualifier.FragmentBannerAd
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {

    @FragmentBannerAd
    @Provides
    fun provideBannerLoader(@ApplicationContext context: Context, fragment: Fragment) =
        MaxBannerLoader(context, fragment)

}