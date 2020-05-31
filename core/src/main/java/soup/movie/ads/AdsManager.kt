package soup.movie.ads

import com.google.android.gms.ads.formats.UnifiedNativeAd

interface AdsManager {

    fun getLoadedNativeAd(): UnifiedNativeAd?

    suspend fun loadNextNativeAd()

    fun onNativeAdConsumed()
}
