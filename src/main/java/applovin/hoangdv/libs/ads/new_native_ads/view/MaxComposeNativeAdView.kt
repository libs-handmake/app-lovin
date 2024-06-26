package applovin.hoangdv.libs.ads.new_native_ads.view

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import applovin.hoangdv.libs.ads.MaxAdViewModel
import applovin.hoangdv.libs.ads.new_native_ads.loader.NativeAdKeeper
import common.hoangdz.lib.extensions.logError
import common.hoangdz.lib.jetpack_compose.exts.SafeModifier
import common.hoangdz.lib.jetpack_compose.exts.collectWhenResume
import common.hoangdz.lib.viewmodels.DataResult
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MaxComposeNativeAdView(
    modifier: Modifier = SafeModifier,
    adViewModel: MaxAdViewModel,
    requestID: String,
    loading: (@Composable () -> Unit)? = null,
    onLoaded: (() -> Unit)? = null,
    androidView: (Context, nativeAD: StateFlow<DataResult<NativeAdKeeper>>, owner: LifecycleOwner) -> MaxNativeAdAndroidView
) {
    var callLoaded by remember {
        mutableStateOf(false)
    }
    val adState = adViewModel.loadNativeAds(requestID)
    val adStateCollector by adState.collectWhenResume()
    val owner = LocalLifecycleOwner.current
    val premiumState by adViewModel.isPremium.collectWhenResume()
    if (adStateCollector.state == DataResult.DataState.ERROR || premiumState) {
        logError("Native error ${adStateCollector.state}")
        return
    }
    Box(SafeModifier.fillMaxWidth()) {
        Box(modifier = modifier) {
            if (adStateCollector.state == DataResult.DataState.LOADED) AndroidView(modifier = SafeModifier.fillMaxWidth(),
                factory = {
                    logError("factory $requestID")
                    androidView(it, adState, owner)
                },
                update = {
                    logError("bind $requestID")
                    it.bindAds(adStateCollector.value)
                    if (callLoaded) return@AndroidView
                    callLoaded = true
                    onLoaded?.invoke()
                })
            if (adStateCollector.state == DataResult.DataState.LOADING || adStateCollector.state == DataResult.DataState.IDLE) loading?.invoke()
        }
    }
}