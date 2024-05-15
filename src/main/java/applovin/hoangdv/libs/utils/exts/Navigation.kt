package applovin.hoangdv.libs.utils.exts

import android.app.Activity
import common.hoangdz.lib.jetpack_compose.navigation.ScreenConfigs
import common.hoangdz.lib.utils.ads.GlobalAdState

fun ScreenConfigs.navigateWithMaxAds(
    route: String,
    activity: Activity?,
    overrideId: String? = null,
    replacement: Boolean = false,
    ignoredAds: Boolean = false
) {
    fun navigate() {
        kotlin.runCatching {
            if (replacement) navigateAndReplace(route)
            else ScreenConfigs.navController?.navigate(route)
        }
    }
    if (ignoredAds) {
        navigate()
        return
    }
    activity?.invokeWithInterstitial(
        this.route.replace("\\?.*".toRegex(), ""), overrideId = overrideId
    ) {
        GlobalAdState.isShowInterForNavigationLastTime = it
        navigate()
    }
}
