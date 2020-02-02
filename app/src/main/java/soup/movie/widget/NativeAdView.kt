package soup.movie.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import soup.movie.R

class NativeAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var nativeAd: UnifiedNativeAd? = null
    private lateinit var nativeAdView: UnifiedNativeAdView

    private lateinit var primaryView: TextView
    private lateinit var secondaryView: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var iconView: ImageView
    private lateinit var callToActionView: Button

    init {
        View.inflate(context, R.layout.native_ad, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        nativeAdView = findViewById(R.id.native_ad_view)
        primaryView = findViewById(R.id.primary)
        secondaryView = findViewById(R.id.secondary)

        ratingBar = findViewById(R.id.rating_bar)
        ratingBar.isEnabled = false

        callToActionView = findViewById(R.id.cta)
        iconView = findViewById(R.id.icon)
    }

    private fun UnifiedNativeAd.hasOnlyStore(): Boolean {
        return store.isNullOrEmpty().not() && advertiser.isNullOrEmpty()
    }

    fun setNativeAd(nativeAd: UnifiedNativeAd) {
        this.nativeAd = nativeAd

        nativeAdView.callToActionView = callToActionView
        nativeAdView.headlineView = primaryView

        secondaryView.visibility = View.VISIBLE
        val advertiser = nativeAd.advertiser
        val secondaryText: String = when {
            nativeAd.hasOnlyStore() -> {
                nativeAdView.storeView = secondaryView
                nativeAd.store
            }
            advertiser.isNullOrEmpty().not() -> {
                nativeAdView.advertiserView = secondaryView
                advertiser
            }
            else -> {
                ""
            }
        }

        primaryView.text = nativeAd.headline
        callToActionView.text = nativeAd.callToAction

        //  Set the secondary view to be the star rating if available.
        val starRating = nativeAd.starRating
        if (starRating != null && starRating > 0) {
            secondaryView.visibility = View.GONE
            ratingBar.visibility = View.VISIBLE
            ratingBar.max = 5
            nativeAdView.starRatingView = ratingBar
        } else {
            secondaryView.text = secondaryText
            secondaryView.visibility = View.VISIBLE
            ratingBar.visibility = View.GONE
        }

        val icon = nativeAd.icon
        if (icon != null) {
            iconView.visibility = View.VISIBLE
            iconView.setImageDrawable(icon.drawable)
        } else {
            iconView.visibility = View.GONE
        }

        nativeAdView.setNativeAd(nativeAd)
    }
}
