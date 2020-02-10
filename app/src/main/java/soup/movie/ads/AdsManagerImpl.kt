package soup.movie.ads

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import soup.movie.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AdsManagerImpl(private val context: Context) : AdsManager {

    private val adUnitId: String = context.getString(R.string.admob_ad_unit_detail)

    override suspend fun loadNativeAd(): UnifiedNativeAd? {
        return suspendCoroutine { continuation ->
            val adLoader = AdLoader.Builder(context, adUnitId)
                .forUnifiedNativeAd { continuation.resume(it) }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        continuation.resume(null)
                    }
                })
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }
}
