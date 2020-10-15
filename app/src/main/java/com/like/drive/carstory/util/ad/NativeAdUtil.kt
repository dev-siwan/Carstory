package com.like.drive.carstory.util.ad

import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.like.drive.carstory.ui.base.ext.getListAdMobId
import timber.log.Timber

class NativeAdUtil {

    private var adLoader: AdLoader? = null
    var nativeAd: UnifiedNativeAd? = null

    fun loadNativeAds(
        context: Context,
        isFirst: Boolean? = false,
        onInitListener: () -> Unit
    ) {
        MobileAds.initialize(context)
        adLoader = AdLoader.Builder(context, getListAdMobId(context)).forUnifiedNativeAd {
            nativeAd = it
            if (isFirst == true) {
                onInitListener()
            }
        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError?) {
                Timber.e(p0?.message)
                if (isFirst == true) {
                    onInitListener()
                }
            }
        }).run {
            build()
        }
        adLoader?.loadAd(AdRequest.Builder().build())

    }

}
