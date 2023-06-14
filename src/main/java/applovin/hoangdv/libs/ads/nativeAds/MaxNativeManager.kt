package applovin.hoangdv.libs.ads.nativeAds

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import applovin.hoangdv.libs.data.shared.AdsShared

class MaxNativeManager(private val context: Context, private val adsShared: AdsShared) {

//    private val adHolders by lazy { mutableListOf<MaxNativeAdHolder>() }

//    init {
//        for (i in 0 until adsShared.numberNativeAdsNeedToLoad) {
//            adHolders.add(MaxNativeAdHolder(context))
//        }
//    }

    fun bindToView(owner: LifecycleOwner, viewGroup: ViewGroup, template: View) {
//        synchronized(adHolders) {
//            if (adHolders.isEmpty()) {
//                viewGroup.isVisible = false
//                return
//            }
//            adHolders.first().bindTemplateToContent(owner, viewGroup, template)
//            adHolders.removeFirst()
//            adHolders.add(MaxNativeAdHolder(context))
//        }

        MaxNativeAdManual(context).bindTemplateToContent(owner, viewGroup, template)
    }
}