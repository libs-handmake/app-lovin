package applovin.hoangdv.libs.data.remote

import applovin.hoangdv.libs.data.shared.AdsShared
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdsRemoteConfigs(private val adsShared: AdsShared) {
    fun fetchAdsConfig() {
        CoroutineScope(Dispatchers.IO).launch {
            val remoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings =
                FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600)
                    .build()
            remoteConfig.apply {
                setConfigSettingsAsync(configSettings)
                setDefaultsAsync(adsShared.firebaseDefaultRemoteConfig)
            }
            remoteConfig.fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful)
                    adsShared.applyFirebaseConfigs(remoteConfig)
            }
        }

    }
}