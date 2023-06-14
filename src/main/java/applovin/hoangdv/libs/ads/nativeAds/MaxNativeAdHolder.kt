package applovin.hoangdv.libs.ads.nativeAds

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import applovin.hoangdv.libs.MaxAds
import applovin.hoangdv.libs.utils.LoaderState
import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.applovin.mediation.nativeAds.adPlacer.MaxAdPlacer
import com.applovin.mediation.nativeAds.adPlacer.MaxAdPlacerSettings
import com.facebook.shimmer.ShimmerFrameLayout

class MaxNativeAdHolder(private val context: Context) :
    LifecycleEventObserver, MaxAdPlacer.Listener {

    private var owner: LifecycleOwner? = null


    private val maxAdSetting by lazy {
        MaxAdPlacerSettings(
            MaxAds.adUnitId?.nativeId ?: return@lazy null
        ).apply {
            maxAdCount = 1
            maxPreloadedAdCount = 1
            fixedPositions.add(0)
        }
    }

    private val adPlacer by lazy { MaxAdPlacer(maxAdSetting, context) }

    private var shimmerLayout: ShimmerFrameLayout? = null

    private var loadingContent: View? = null

    private var containerView: ViewGroup? = null

    private var loaderState = LoaderState.IDLE

    init {
        initListener()
        startLoadAd()
    }


    private fun startBindNativeAd() {

    }

    private fun startLoadAd() {
        if (loaderState == LoaderState.LOADED) return
        if (loaderState == LoaderState.LOADING) return
        updateState(LoaderState.LOADING)
        adPlacer.loadAds()
    }


    fun bindTemplateToContent(owner: LifecycleOwner, container: ViewGroup, template: View) {
        this.owner = owner
        this.containerView = container
        val binder = template.run {
            MaxNativeAdViewBinder.Builder(template)
                .setTitleTextViewId(
                    getIdentifier("ad_headline")
                )
                .setBodyTextViewId(getIdentifier("ad_body"))
                .setCallToActionButtonId(getIdentifier("ad_call_to_action"))
                .setIconImageViewId(getIdentifier("icon_view"))
//            .setMediaContentViewGroupId(getIdentifier("media_view"))
//                .setAdvertiserTextViewId(getIdentifier("cv_ad"))
                .build()
        }
        shimmerLayout = template.findViewById(getIdentifier("shimmerFrameLayout"))
        loadingContent = template.findViewById(getIdentifier("loading_layout"))
        adPlacer.setNativeAdViewBinder(binder)
        container.removeAllViews()
        container.addView(template)
        adPlacer.renderAd(0, container)
        updateState(loaderState)
        startLoadAd()
    }

    private fun updateState(state: LoaderState) {
        loaderState = state
        Log.e("updateState: ${hashCode()}", state.toString())
        owner?.lifecycleScope?.launchWhenResumed {
            when (state) {
                LoaderState.IDLE, LoaderState.LOADING -> {
                    shimmerLayout?.apply {
                        isVisible = true
                        showShimmer(true)
                    }
                    loadingContent?.isVisible = true
                }

                LoaderState.LOADED -> {
                    shimmerLayout?.apply {
                        isVisible = true
                        stopShimmer()
                        hideShimmer()
                    }
                    adPlacer.renderAd(0, containerView)
                    loadingContent?.isVisible = false
                }

                LoaderState.ERROR -> {
                    containerView?.isVisible = false
                    loadingContent?.isVisible = false
                }
            }
        }

    }

    private fun getIdentifier(id: String): Int {
        return context.resources.getIdentifier(
            id,
            "id",
            context.packageName
        )
    }

    private fun initListener() {
        owner?.lifecycle?.addObserver(this)
        adPlacer.setListener(this)
    }

    override fun onAdLoaded(p0: Int) {
//        Log.e("onAdLoaded: ", p0.toString())
//        when (p0) {
//            1 -> updateState(LoaderState.LOADED)
//            0, 2 -> updateState(LoaderState.ERROR)
//        }
        updateState(LoaderState.LOADED)
    }

    override fun onAdRemoved(p0: Int) {
        updateState(LoaderState.ERROR)
        Log.e("onAdClicked: ", "trigger")
    }

    override fun onAdClicked(p0: MaxAd?) {
        Log.e("onAdClicked: ", "trigger")
    }

    override fun onAdRevenuePaid(p0: MaxAd?) {
        Log.e("onAdClicked: ", "trigger")
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY)
            adPlacer.destroy()
    }
}