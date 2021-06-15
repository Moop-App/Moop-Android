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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import soup.movie.R
import timber.log.Timber

class AdsManagerImpl(private val context: Context) : AdsManager {

    private val adUnitId: String = context.getString(R.string.admob_ad_unit_detail)

    enum class State {
        LOADED, CONSUMED
    }
    private var state: State = State.CONSUMED
    private var lastNativeAd: UnifiedNativeAd? = null

    init {
        GlobalScope.launch(Dispatchers.IO) {
            MobileAds.initialize(context)
        }
    }

    override fun getLoadedNativeAd(): UnifiedNativeAd? {
        Timber.d("getLoadedNativeAd: state=$state")
        return lastNativeAd
    }

    override suspend fun loadNextNativeAd() {
        if (state == State.LOADED) {
            return
        }

        val adLoader = AdLoader.Builder(context, adUnitId)
            .forUnifiedNativeAd {
                lastNativeAd = it
                state = State.LOADED
                Timber.d("loadNextNativeAd: State.LOADED")
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) {
                    Timber.w("onAdFailedToLoad: errorCode=$errorCode")
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
