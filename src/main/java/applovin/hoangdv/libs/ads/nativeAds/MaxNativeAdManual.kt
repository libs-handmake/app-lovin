package applovin.hoangdv.libs.ads.nativeAds

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import applovin.hoangdv.libs.MaxAds
import applovin.hoangdv.libs.utils.LoaderState
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdRevenueListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import common.hoangdz.lib.R
import common.hoangdz.lib.extensions.launchWhen

internal class MaxNativeAdManual(private val context: Context) : MaxNativeAdListener(),
    LifecycleEventObserver, MaxAdRevenueListener {

    private var owner: LifecycleOwner? = null

    private val adLoader by lazy {
        MaxNativeAdLoader(
            MaxAds.adUnitId?.nativeId ?: return@lazy null, context
        )
    }

    private var loadingLayout: View? = null

    private var contentLayout: View? = null

    private var containerView: ViewGroup? = null

    private var loaderState = LoaderState.IDLE

    private var maxAd: MaxAd? = null

    init {
        initListener()
    }


    private fun startLoadAd() {
        if (loaderState == LoaderState.LOADED) return
        if (loaderState == LoaderState.LOADING) return
        updateState(LoaderState.LOADING)
        adLoader?.loadAd()
    }


    fun bindTemplateToContent(owner: LifecycleOwner, container: ViewGroup, template: View) {
        this.owner = owner
        owner.lifecycle.addObserver(this)
        this.containerView = container
        val binder = template.run {
            MaxNativeAdViewBinder.Builder(template).setTitleTextViewId(
                R.id.head_line_view
            ).setBodyTextViewId(R.id.content_view).setCallToActionButtonId(R.id.call_to_action_view)
                .setIconImageViewId(R.id.icon_view).setMediaContentViewGroupId(R.id.media_view)
//                .setAdvertiserTextViewId(getIdentifier("tv_ad"))
                .build()
        }
        contentLayout = template.findViewById(R.id.layout_ads)
        loadingLayout = template.findViewById(R.id.layout_shimmer)
        container.removeAllViews()
        val maxAdView = MaxNativeAdView(binder, context)
        container.addView(maxAdView)
        updateState(loaderState)
        startLoadAd()
    }


    override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd) {
//        adLoader?.destroy(p1)
        updateState(LoaderState.LOADED)
    }

    override fun onNativeAdLoadFailed(p0: String, p1: MaxError) {
        updateState(LoaderState.ERROR)
    }

    override fun onNativeAdClicked(p0: MaxAd) {
    }

    override fun onNativeAdExpired(p0: MaxAd) {
        updateState(LoaderState.ERROR)
    }

    private fun updateState(state: LoaderState) {
        loaderState = state
        owner?.launchWhen(Lifecycle.State.RESUMED) {
            when (state) {
                LoaderState.IDLE, LoaderState.LOADING -> {
                    contentLayout?.isVisible = false
                    loadingLayout?.isVisible = true
                }

                LoaderState.LOADED -> {
                    contentLayout?.isVisible = true
                    loadingLayout?.isVisible = false
                }

                LoaderState.ERROR -> {
                    containerView?.isVisible = false
                }
            }
        }

    }

    private fun initListener() {
        owner?.lifecycle?.addObserver(this)
        adLoader?.setNativeAdListener(this)
        adLoader?.setRevenueListener(this)
    }

    override fun onAdRevenuePaid(p0: MaxAd) {

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            adLoader?.destroy()
        }
    }
}