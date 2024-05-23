package applovin.hoangdv.libs.utils

import com.applovin.mediation.MaxAd

object MaxAdState {
    var overrideInterGap: Long? = null

    var onAdPaidEvent: ((MaxAd) -> Unit)? = null
}