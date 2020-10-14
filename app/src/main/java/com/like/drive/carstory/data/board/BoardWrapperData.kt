package com.like.drive.carstory.data.board

import androidx.annotation.Keep
import com.google.android.gms.ads.formats.UnifiedNativeAd

@Keep
class BoardWrapperData(
    var boardData: BoardData? = null,
    var nativeAd: UnifiedNativeAd? = null
)