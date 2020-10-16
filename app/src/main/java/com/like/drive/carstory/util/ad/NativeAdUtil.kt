package com.like.drive.carstory.util.ad

import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.like.drive.carstory.ui.base.ext.getListAdMobId
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class NativeAdUtil {

    private var adLoader: AdLoader? = null
    var nativeAd: UnifiedNativeAd? = null
    private val nativeAdList = ArrayList<UnifiedNativeAd>()
    private val random = Random()

    fun loadNativeAds(
        context: Context,
        onLoadNativeAd: () -> Unit
    ) {
        MobileAds.initialize(context)
        adLoader = AdLoader.Builder(context, getListAdMobId(context)).forUnifiedNativeAd {
            nativeAdList.add(it)

            if (nativeAdList.isNotEmpty()) {
                val num = random.nextInt(nativeAdList.size)
                nativeAd = nativeAdList[num]
            }

            onLoadNativeAd.invoke()

        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError?) {
                error?.let {
                    Timber.e("Response NativeAd Error == ${it.message}")
                }
                onLoadNativeAd.invoke()
            }
        }).run {
            build()
        }
        adLoader?.loadAd(AdRequest.Builder().build())

    }

}
