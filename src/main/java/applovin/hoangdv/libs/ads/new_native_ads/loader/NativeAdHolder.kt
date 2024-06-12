package applovin.hoangdv.libs.ads.new_native_ads.loader

class NativeAdHolder {
    private val savedNativeAds by lazy { mutableListOf<NativeAdKeeper>() }

    val availableNativeAd
        get() = synchronized(savedNativeAds) {
            savedNativeAds.size
        }


    fun appendNativeAd(vararg adKeepers: NativeAdKeeper) {
        synchronized(savedNativeAds) {
            savedNativeAds.addAll(adKeepers)
        }
    }

    fun getNativeAd(loaderId: String): NativeAdKeeper? {
        return synchronized(savedNativeAds) {
            return@synchronized savedNativeAds.removeFirstOrNull()?.also { it.loaderID = loaderId }
        }
    }

    fun release() {
        synchronized(savedNativeAds) {
            for (nativeAd in savedNativeAds) {
                nativeAd.destroy()
            }
            savedNativeAds.clear()
        }
    }
}