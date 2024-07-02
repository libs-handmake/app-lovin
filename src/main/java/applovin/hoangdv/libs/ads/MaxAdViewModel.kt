package applovin.hoangdv.libs.ads

import android.app.Application
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.viewModelScope
import applovin.hoangdv.libs.ads.new_native_ads.loader.NativeAdKeeper
import applovin.hoangdv.libs.ads.new_native_ads.loader.NativeAdQueue
import applovin.hoangdv.libs.ads.new_native_ads.loader.NativeAdsLoader
import common.hoangdz.lib.extensions.launchIO
import common.hoangdz.lib.utils.user.PremiumHolder
import common.hoangdz.lib.viewmodels.AppViewModel
import common.hoangdz.lib.viewmodels.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MaxAdViewModel @Inject constructor(
    application: Application,
    private val premiumHolder: PremiumHolder,
    private val nativeAdsLoader: NativeAdsLoader
) : AppViewModel(application) {
    val isPremium get() = premiumHolder.premiumState

    val isPremiumValue get() = premiumHolder.isPremium

    private val nativeAdMapper by lazy { hashMapOf<String, MutableStateFlow<DataResult<NativeAdKeeper>>>() }

    fun loadNativeAds(requestId: String): MutableStateFlow<DataResult<NativeAdKeeper>> {
        val nativeLoaderState = synchronized(nativeAdMapper) {
            nativeAdMapper[requestId] ?: MutableStateFlow(
                DataResult<NativeAdKeeper>(
                    DataResult.DataState.IDLE
                )
            ).also { nativeAdMapper[requestId] = it }
        }
        viewModelScope.launchIO {
            nativeAdsLoader.enqueueNativeAds(NativeAdQueue(requestId, nativeLoaderState))
        }
        return nativeLoaderState
    }

    override fun onCleared() {
        super.onCleared()
        synchronized(nativeAdMapper) {
            nativeAdMapper.entries.forEach {
                it.value.value.value?.destroy()
            }
            nativeAdMapper.clear()
        }
    }

    fun registerReloadNative() {
        synchronized(nativeAdMapper) {
            for (entry in nativeAdMapper) {
                entry.value.value?.value?.destroy()
                entry.value.value = DataResult(DataResult.DataState.IDLE)
            }
//            nativeAdMapper.remove(idReload)
        }
    }
}

val LocalMaxAdViewModel = compositionLocalOf<MaxAdViewModel?> { null }