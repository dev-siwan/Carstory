package com.like.drive.carstory.util.ad

import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.like.drive.carstory.ui.base.ext.getNativeAdMobId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.suspendCoroutine

class NativeAdUtil {

    private val nativeAdList = arrayListOf<UnifiedNativeAd>()
    private var adLoader: AdLoader? = null

    fun init(
        context: Context,
        scope: CoroutineScope,
        onInsertList: (ArrayList<UnifiedNativeAd>) -> Unit
    ) {
        if (nativeAdList.isNotEmpty()) {
            nativeAdList.clear()
        }

        scope.launch {
            loadNativeAds(context).catch { e ->
                Timber.i("error===${e.message}")
            }.collect {
                onInsertList(it)
            }
        }

    }

    private suspend fun loadNativeAds(context: Context): Flow<ArrayList<UnifiedNativeAd>> =
        flow {
            MobileAds.initialize(context)
            adLoader = AdLoader.Builder(context, getNativeAdMobId(context))
                .apply {
                    forUnifiedNativeAd {
                        nativeAdList.add(it)
                        if (adLoader?.isLoading == true) {
                            suspend {
                                emit(nativeAdList)
                            }
                        }
                    }
                }.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(p0: LoadAdError?) {
                        Timber.e(p0?.message)
                        if (adLoader?.isLoading == true) {
                            suspend {
                                emit(nativeAdList)
                            }
                        }
                    }
                }).run {
                    build()
                }
            adLoader?.loadAd(AdRequest.Builder().build())

        }

}