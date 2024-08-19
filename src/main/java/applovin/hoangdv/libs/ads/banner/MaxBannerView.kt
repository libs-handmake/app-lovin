package applovin.hoangdv.libs.ads.banner

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import applovin.hoangdv.libs.databinding.TemplateBannerAdBinding
import common.hoangdz.lib.di.entry_point.CommonAppEntryPoint
import common.hoangdz.lib.extensions.appInject
import common.hoangdz.lib.extensions.layoutInflater
import common.hoangdz.lib.jetpack_compose.exts.SafeModifier
import common.hoangdz.lib.jetpack_compose.exts.collectWhenResume

@Composable
fun MaxBannerView() {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val premiumHolder = remember {
        context.appInject<CommonAppEntryPoint>().premiumHolder()
    }
    val maxBannerLoader = remember {
        MaxBannerLoader(context, owner)
    }
    DisposableEffect(key1 = Unit) {
        onDispose {
            maxBannerLoader.destroy()
        }
    }
    val isPremium by premiumHolder.premiumState.collectWhenResume()
    if (!isPremium) AndroidView(factory = {
        val binding = TemplateBannerAdBinding.inflate(it.layoutInflater)
        maxBannerLoader.loadAd(binding)
        binding.root
    }, modifier = SafeModifier.fillMaxWidth())
}