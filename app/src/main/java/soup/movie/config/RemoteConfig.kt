/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import soup.movie.R

class RemoteConfig : Config {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        setConfigSettingsAsync(configSettings)
        setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override fun fetchAndActivate(onComplete: () -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                onComplete()
            }
    }

    override val allowToRunLegacyWorker: Boolean
        get() = remoteConfig.getBoolean("allow_to_run_legacy_worker")
}
