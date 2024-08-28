package applovin.hoangdv.libs.ads.new_native_ads.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import applovin.hoangdv.libs.ads.new_native_ads.loader.NativeAdKeeper
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import common.hoangdz.lib.R
import common.hoangdz.lib.extensions.gone
import common.hoangdz.lib.extensions.layoutInflater
import common.hoangdz.lib.extensions.logError
import common.hoangdz.lib.extensions.visible

class MaxNativeAdAndroidView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var nativeAd: NativeAdKeeper? = null

    private var nativeAdBindingView: MaxNativeAdView? = null

    private var headLineBindingView: TextView? = null

    private var contentBindingView: TextView? = null

    private var mediaBindingView: FrameLayout? = null

    private var iconBindingView: ImageView? = null

    private var callToActionBindingView: TextView? = null

    private var adLabelView: TextView? = null

    private var res: Int? = null

    private var contentView: View? = null

    fun setContentView(@LayoutRes layoutRes: Int) {
        res = layoutRes
    }

    private fun bindView() {
        adLabelView = contentView?.findViewById(R.id.tv_ad_label)
        headLineBindingView = contentView?.findViewById(R.id.head_line_view)
        contentBindingView = contentView?.findViewById(R.id.content_view)
        iconBindingView = contentView?.findViewById(R.id.icon_view)
        mediaBindingView = contentView?.findViewById(R.id.media_view)
        callToActionBindingView = contentView?.findViewById(R.id.call_to_action_view)


    }

    fun bindAds(nativeAd: NativeAdKeeper?) {
        synchronized(this) {
            if (nativeAd != null && this.nativeAd == nativeAd) return
            this.nativeAd = nativeAd
            if (res == null) {
                logError("Not found template layout id ")
            }
            prepare()
            nativeAd ?: kotlin.run {
                logError("Native ad not available")
                gone()
                return
            }
            startBindAds(nativeAd)
        }
    }

    private fun startBindAds(nativeAd: NativeAdKeeper?) {
        nativeAd ?: kotlin.run {
            gone()
            return
        }
        visible()
        nativeAd.render(nativeAdBindingView ?: return)
    }

    private fun prepare() {
        removeAllViews()
        val view =
            context.layoutInflater.inflate(res ?: return, this, false).also { contentView = it }
        view.visible()
        bindView()
        val binder = MaxNativeAdViewBinder.Builder(view).setTitleTextViewId(
            R.id.head_line_view
        ).setBodyTextViewId(R.id.content_view).setCallToActionButtonId(R.id.call_to_action_view)
            .setIconImageViewId(R.id.icon_view).run {
                if (mediaBindingView == null) this else setMediaContentViewGroupId(R.id.media_view)
            }
//                .setAdvertiserTextViewId(getIdentifier("tv_ad"))
            .build()

        nativeAdBindingView = MaxNativeAdView(binder, context)
        addView(nativeAdBindingView)
    }
}