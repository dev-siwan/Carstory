package com.like.drive.carstory.ui.board.list.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.like.drive.carstory.databinding.HolderListAdvBinding

class ListAdvHolder(val binding: HolderListAdvBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(ad: UnifiedNativeAd?) {
        ad?.let {
            populateUnifiedNativeAdView(it, binding.adView)
            binding.executePendingBindings()
        }
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