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
package soup.movie.ads

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import soup.movie.R
import timber.log.Timber

class AdsManagerImpl(
    private val context: Context,
    lifecycleOwner: LifecycleOwner
) : AdsManager {

    private val adUnitId: String = context.getString(R.string.admob_ad_unit_detail)

    enum class State {
        LOADED, CONSUMED
    }

    private var state: State = State.CONSUMED
    private var lastNativeAd: NativeAd? = null

    init {
        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(context)
        }
    }

    override fun getLoadedNativeAd(): NativeAd? {
        Timber.d("getLoadedNativeAd: state=$state")
        return lastNativeAd
    }

    override suspend fun loadNextNativeAd() {
        if (state == State.LOADED) {
            return
        }

        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd {
                lastNativeAd = it
                state = State.LOADED
                Timber.d("loadNextNativeAd: State.LOADED")
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Timber.w("onAdFailedToLoad: errorCode=${error.code}")
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    override fun onNativeAdConsumed() {
        Timber.d("onNativeAdConsumed: State.CONSUMED")
        state = State.CONSUMED
    }
}
