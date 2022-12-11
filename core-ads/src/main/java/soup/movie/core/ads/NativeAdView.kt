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

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

internal class NativeAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var nativeAd: NativeAd? = null

    private val adView: NativeAdView
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

    fun setNativeAd(nativeAd: NativeAd) {
        this.nativeAd = nativeAd

        headlineView.text = nativeAd.headline

        val secondaryText: String? = when {
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
        if (secondaryText.isNullOrEmpty()) {
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
            starRatingView.rating = starRating.toFloat()
            starRatingView.visibility = View.VISIBLE
        }

        adView.setNativeAd(nativeAd)
    }
}
