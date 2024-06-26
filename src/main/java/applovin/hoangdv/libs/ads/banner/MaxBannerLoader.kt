package applovin.hoangdv.libs.ads.banner

import android.content.Context
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import applovin.hoangdv.libs.MaxAds
import applovin.hoangdv.libs.databinding.TemplateBannerAdBinding
import applovin.hoangdv.libs.utils.MaxAdState
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdkUtils
import common.hoangdz.lib.extensions.getActivity
import common.hoangdz.lib.extensions.launchWhen


class MaxBannerLoader(private val context: Context, private val owner: LifecycleOwner) :
    MaxAdViewAdListener {

    private val maxAdView by lazy {
        MaxAdView(
            MaxAds.adUnitId?.bannerID ?: return@lazy null, context
        )
    }

    private var binding: TemplateBannerAdBinding? = null

    fun loadAd(binding: TemplateBannerAdBinding) {
        this.binding = binding
        val heightDp = MaxAdFormat.BANNER.getAdaptiveSize(context.getActivity() ?: return).height
        val heightPx = AppLovinSdkUtils.dpToPx(context, heightDp)
        maxAdView?.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, heightPx
        )
        maxAdView?.setExtraParameter("adaptive_banner", "true")
        maxAdView?.setLocalExtraParameter("adaptive_banner_width", 400)
//        maxAdView?.adFormat?.getAdaptiveSize(400, context)?.height?.let {
//            if (it > 0) maxAdView?.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, it
//            )
//        }
        maxAdView?.setBackgroundColor(android.graphics.Color.BLACK)
        maxAdView?.setListener(this)
        maxAdView?.setRevenueListener {
            MaxAdState.onAdPaidEvent?.invoke(it)
        }
        binding.frameContainer.addView(maxAdView)
        maxAdView?.loadAd()
    }

    override fun onAdLoaded(p0: MaxAd) {
        owner.launchWhen(Lifecycle.State.RESUMED) {
            binding?.root?.isVisible = true
            binding?.shimmerFrameLayout?.apply {
                stopShimmer()
                hideShimmer()
                isVisible = false
            }
        }
    }

    override fun onAdDisplayed(p0: MaxAd) {
    }

    override fun onAdHidden(p0: MaxAd) {
    }

    override fun onAdClicked(p0: MaxAd) {
    }

    override fun onAdLoadFailed(p0: String, p1: MaxError) {
        owner.launchWhen(Lifecycle.State.RESUMED) {
            binding?.root?.isVisible = false
            binding?.shimmerFrameLayout?.apply {
                stopShimmer()
                hideShimmer()
            }
        }
    }

    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
    }

    override fun onAdExpanded(p0: MaxAd) {
    }

    override fun onAdCollapsed(p0: MaxAd) {
    }

    fun destroy() {
        maxAdView?.destroy()
    }
}