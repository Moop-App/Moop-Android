package soup.movie.detail.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import soup.movie.detail.R

class NativeAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var nativeAd: UnifiedNativeAd? = null

    private val adView: UnifiedNativeAdView
    private val headlineView: TextView
    private val bodyView: TextView
    private val callToActionView: Button
    private val iconView: ImageView
    private val priceView: TextView
    private val starRatingView: RatingBar

    init {
        View.inflate(context, R.layout.native_ad, this)
        adView = findViewById(R.id.native_ad_view)

        headlineView = findViewById(R.id.ad_headline)
        adView.headlineView = headlineView

        bodyView = findViewById(R.id.ad_body)

        callToActionView = findViewById(R.id.ad_call_to_action)
        adView.callToActionView = callToActionView

        iconView = findViewById(R.id.ad_icon)
        adView.iconView = iconView

        priceView = adView.findViewById(R.id.ad_price)
        adView.priceView = priceView

        starRatingView = adView.findViewById(R.id.ad_stars)
        adView.starRatingView = starRatingView
    }

    fun setNativeAd(nativeAd: UnifiedNativeAd) {
        this.nativeAd = nativeAd

        headlineView.text = nativeAd.headline

        val secondaryText: String = when {
            nativeAd.advertiser.isNullOrEmpty().not() -> {
                adView.advertiserView = bodyView
                nativeAd.advertiser
            }
            nativeAd.store.isNullOrEmpty().not() -> {
                adView.storeView = bodyView
                nativeAd.store
            }
            else -> {
                adView.bodyView = bodyView
                nativeAd.body
            }
        }
        if (secondaryText.isEmpty()) {
            bodyView.visibility = View.GONE
        } else {
            bodyView.visibility = View.VISIBLE
            bodyView.text = secondaryText
        }

        callToActionView.text = nativeAd.callToAction

        val icon = nativeAd.icon
        if (icon == null) {
            iconView.visibility = View.INVISIBLE
        } else {
            iconView.setImageDrawable(icon.drawable)
            iconView.visibility = View.VISIBLE
        }

        val price = nativeAd.price
        if (price == null) {
            priceView.visibility = View.INVISIBLE
        } else {
            priceView.visibility = View.VISIBLE
            priceView.text = price
        }

        val starRating = nativeAd.starRating
        if (starRating == null) {
            starRatingView.visibility = View.GONE
        } else {
            starRatingView.rating = nativeAd.starRating.toFloat()
            starRatingView.visibility = View.VISIBLE
        }

        adView.setNativeAd(nativeAd)
    }
}
