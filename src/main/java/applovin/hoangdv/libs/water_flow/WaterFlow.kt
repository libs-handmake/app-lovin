package applovin.hoangdv.libs.water_flow

import applovin.hoangdv.libs.data.shared.MaxAdsLibShared
import kotlin.math.max
import kotlin.math.min


class WaterFlow(
    private val ids: MutableList<String>,
    private val normalID: String,
    private val adsShared: MaxAdsLibShared
) {
    companion object {
        var forceTurnOffWaterFlow = false
    }

    init {
        reset()
    }

    private var adsFailTime = 0L

    private var adsBlockToLoadTime = 0L

    private var currentIndex = 0

    val currentId get() = if (adsShared.isMonetization) ids[currentIndex] else normalID

    private val canNext get() = currentIndex < ids.lastIndex

    val canShowAds
        get() = (System.currentTimeMillis() - adsFailTime > adsBlockToLoadTime)

    private fun next() {
        currentIndex++
    }

    fun failed(onNext: () -> Unit) {
        if (canNext) {
            next()
            onNext()
        } else {
            adsFailTime = System.currentTimeMillis()
            adsBlockToLoadTime = max(
                min(adsBlockToLoadTime * 2, adsShared.maxWaterFlowGap),
                adsShared.minWaterFlowGap
            )
            currentIndex = 0
        }
    }

    fun reset() {
        currentIndex = 0
        adsBlockToLoadTime = 0
    }
}