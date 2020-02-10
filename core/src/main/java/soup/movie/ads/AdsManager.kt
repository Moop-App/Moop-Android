package soup.movie.ads

import com.google.android.gms.ads.formats.UnifiedNativeAd

interface AdsManager {

    suspend fun loadNativeAd(): UnifiedNativeAd?
}
