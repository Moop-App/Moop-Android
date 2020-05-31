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
