package applovin.hoangdv.libs.ads.banner

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import applovin.hoangdv.libs.databinding.TemplateBannerAdBinding
import common.hoangdz.lib.extensions.layoutInflater
import common.hoangdz.lib.jetpack_compose.exts.SafeModifier

@Composable
fun MaxBannerView() {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val maxBannerLoader = remember {
        MaxBannerLoader(context, owner)
    }
    AndroidView(factory = {
        val binding = TemplateBannerAdBinding.inflate(it.layoutInflater)
        maxBannerLoader.loadAd(binding)
        binding.root
    }, modifier = SafeModifier.fillMaxWidth())
}