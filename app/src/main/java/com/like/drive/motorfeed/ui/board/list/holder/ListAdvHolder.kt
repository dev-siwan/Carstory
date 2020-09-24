package com.like.drive.motorfeed.ui.board.list.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.like.drive.motorfeed.databinding.HolderListAdvBinding
import timber.log.Timber

class ListAdvHolder(val binding: HolderListAdvBinding) : RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context

    fun bind() {
        MobileAds.initialize(context)
        val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
            .apply {
                forUnifiedNativeAd {
                    populateUnifiedNativeAdView(it, binding.adView)
                }
            }.withAdListener(object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    binding.adView.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    super.onAdFailedToLoad(p0)
                    val a = p0?.message
                    Timber.e(a)
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

        adView.apply {
            mediaView = binding.adMedia
            headlineView = binding.adHeadLine
            bodyView = binding.adBody
            (headlineView as TextView).text = nativeAd.headline
            mediaView.setMediaContent(nativeAd.mediaContent)
            if (nativeAd.body == null) {
                adView.bodyView.visibility = View.INVISIBLE
            } else {
                bodyView.visibility = View.VISIBLE
                (bodyView as TextView).text = nativeAd.body
            }
        }.run {
            setNativeAd(nativeAd)
        }

    }

    companion object {
        fun from(parent: ViewGroup): ListAdvHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderListAdvBinding.inflate(layoutInflater, parent, false)

            return ListAdvHolder(binding)
        }
    }
}