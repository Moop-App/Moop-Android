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
package soup.movie.core.ads

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.common.IoDispatcher
import soup.movie.log.Logger
import javax.inject.Inject

@SuppressLint("MissingPermission")
class AdsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val config: AdsConfig,
) : AdsManager {

    enum class State {
        LOADED, CONSUMED
    }

    private var state: State = State.CONSUMED
    private var lastNativeAd: NativeAd? = null

    init {
        ProcessLifecycleOwner.get().lifecycleScope.launch(ioDispatcher) {
            MobileAds.initialize(context)
        }
    }

    override fun getLoadedNativeAd(): NativeAdInfo? {
        Logger.d("getLoadedNativeAd: state=$state")
        return lastNativeAd?.let { NativeAdInfo(it) }
    }

    override suspend fun loadNextNativeAd() {
        withContext(ioDispatcher) {
            if (state == State.LOADED) {
                return@withContext
            }

            val adLoader = AdLoader.Builder(context, config.detailAdUnitId)
                .forNativeAd {
                    lastNativeAd = it
                    state = State.LOADED
                    Logger.d("loadNextNativeAd: State.LOADED")
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Logger.w("onAdFailedToLoad: errorCode=${error.code}")
                    }
                })
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

    override fun onNativeAdConsumed() {
        Logger.d("onNativeAdConsumed: State.CONSUMED")
        state = State.CONSUMED
    }
}
