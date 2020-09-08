package com.like.drive.motorfeed.ui.feed.list.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.like.drive.motorfeed.databinding.HolderListAdvBinding

class FeedListAdvHolder(val binding: HolderListAdvBinding) : RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context

    fun bind() {
        MobileAds.initialize(context)
        val adLoader = AdLoader.Builder(context, "ca-app-pub-6215810193035158/3935816982")
            .apply {
                forUnifiedNativeAd {
                    populateUnifiedNativeAdView(it, binding.adView)
                }
            }.withAdListener(object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    binding.rootView.visibility = View.VISIBLE
                }
            }).run {
                build()
            }

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateUnifiedNativeAdView(
        nativeAd: UnifiedNativeAd,
        adView: UnifiedNativeAdView
    ) {
        // You must call destroy on old ads when you are done with them,
        // otherwise you will have a memory leak.
        /*currentNativeAd?.destroy()
        currentNativeAd = nativeAd*/
        // Set the media view.
        adView.mediaView = binding.adMedia

        // Set other ad assets.
        adView.headlineView = binding.adHeadLine
        adView.bodyView = binding.adBody
        // adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = binding.adAppIcon
        /*  adView.priceView = adView.findViewById(R.id.ad_price)
          adView.starRatingView = adView.findViewById(R.id.ad_stars)
          adView.storeView = adView.findViewById(R.id.ad_store)
          adView.advertiserView = adView.findViewById(R.id.ad_advertiser)*/

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView.setMediaContent(nativeAd.mediaContent)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        /* if (nativeAd.callToAction == null) {
             adView.callToActionView.visibility = View.INVISIBLE
         } else {
             adView.callToActionView.visibility = View.VISIBLE
             (adView.callToActionView as Button).text = nativeAd.callToAction
         }*/

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE
        }

        /*if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }*/

        /*if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }*/

        /*if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }*/

        /*if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }*/

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)

    }

    companion object {
        fun from(parent: ViewGroup): FeedListAdvHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderListAdvBinding.inflate(layoutInflater, parent, false)

            return FeedListAdvHolder(binding)
        }
    }
}