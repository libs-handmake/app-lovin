package applovin.hoangdv.libs.ads.new_native_ads.loader

import common.hoangdz.lib.viewmodels.DataResult
import kotlinx.coroutines.flow.MutableStateFlow

data class NativeAdQueue(
    val queueID: String, val adState: MutableStateFlow<DataResult<NativeAdKeeper>>
)