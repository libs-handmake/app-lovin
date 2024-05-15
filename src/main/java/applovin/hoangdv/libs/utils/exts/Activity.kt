package applovin.hoangdv.libs.utils.exts

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import applovin.hoangdv.libs.di.entry_point.ActivityEntryPoint
import common.hoangdz.lib.extensions.activityInject
import common.hoangdz.lib.extensions.launchIO
import common.hoangdz.lib.extensions.launchWhen
import common.hoangdz.lib.lifecycle.ActivityLifecycleManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay

@OptIn(DelicateCoroutinesApi::class)
fun Activity.invokeWithInterstitial(
    screenName: String, overrideId: String? = null, onInterPassed: (Boolean) -> Unit
) {
    if (this is AppCompatActivity) {
        val interLoader = activityInject<ActivityEntryPoint>().maxInterstitialManager()
        interLoader.show { showed ->
            GlobalScope.launchIO {
                var owner: LifecycleOwner? = null
                var tryAgain = 0
                while (ActivityLifecycleManager[this@invokeWithInterstitial]?.also {
                        owner = it
                    } == null) {
                    if (tryAgain == 3) return@launchIO
                    tryAgain++
                    delay(500)
                }
                owner?.launchWhen(
                    Lifecycle.State.RESUMED
                ) {
                    onInterPassed(showed)
                }
            }
        }
    } else onInterPassed(false)
}