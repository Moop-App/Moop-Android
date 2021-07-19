package soup.movie.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import soup.movie.R

class RemoteConfig: Config {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        setConfigSettingsAsync(configSettings)
        setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override fun fetchAndActivate(onComplete: () -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                onComplete()
            }
    }

    override val allowToRunLegacyWorker: Boolean
        get() = remoteConfig.getBoolean("allow_to_run_legacy_worker")
}
