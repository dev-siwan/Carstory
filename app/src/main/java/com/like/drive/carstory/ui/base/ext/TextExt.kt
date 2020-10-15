package com.like.drive.carstory.ui.base.ext

import android.content.Context
import android.text.Html
import com.like.drive.carstory.BuildConfig
import com.like.drive.carstory.R

//이름
fun String.isNickName(): Boolean {
    val regex = Regex("^.{1,5}[가-힣]$")
    return this.matches(regex)
}

//생년월일
fun String.isUserBirth(): Boolean {
    val regex = Regex("^(19\\d{2}|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\$")
    return this.matches(regex)
}

//일
fun String.isDay(): Boolean {
    val regex = Regex("^(0[1-9]|[1-2][0-9]|3[0-1])\$")
    return this.matches(regex)
}

//패스워드
fun String.isPassword(): Boolean {
    val regex =
        Regex("^(?=.*\\d)(?=.*[~`!@#\$%^&*()-])(?=.*[a-zA-Z])[a-zA-Z\\d~`!@#\$%^&*()-]{8,20}\$")
    return this.matches(regex)
}

//주민번호
fun String.isNumberID(): Boolean {
    val regex = Regex("^(19\\d{2}|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])[1-8]\$")
    return this.matches(regex)
}

//전화번호
fun String.isCallNumber(): Boolean {
    val regex =
        Regex("^(02|03[1|2|3]|04[1|2|3|4]|05[1|2|3|4|5]|06[1|2|3|4]|070|01[0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})\$")
    return this.matches(regex)
}

//휴대전화
fun String.isPhoneNumber(): Boolean {
    val regex =
        Regex("^(02|03[1|2|3]|04[1|2|3|4]|05[1|2|3|4|5]|06[1|2|3|4]|070|01[0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})\$")
    return this.matches(regex)
}

//이메일
fun String.isEmail(): Boolean {
    return !this.isBlank() && this.contains('@')
}

//전화번호 포함여부
fun String.isIncludePhoneNumber(): Boolean {
    val regex =
        Regex("(02|03[1|2|3]|04[1|2|3|4]|05[1|2|3|4|5]|06[1|2|3|4]|070|01[0|1|6|7|8|9])[\\s\\n./-]*([0-9]{3,4})[\\s\\n./-]*([0-9]{4})")
    val matchResult: MatchResult? = regex.find(this)
    return matchResult != null

}

//금지어 포함여부
fun String.isIncludeBadKeyword(): List<String> {
    val badKeywords = listOf(
        "중개수수료",
        "수수료",
        "복비",
        "공짜",
        "꽁짜",
        "무료",
        "반값",
        "셰어",
        "쉐어",
        "메이트",
        "중개료",
        "하메",
        "사무실",
        "작업실",
        "share",
        "mate",
        "원룸텔",
        "다방",
        "실입주금",
        "관리비",
        "대출",
        "이자"
    )

    return badKeywords.filter { keyword -> this.contains(keyword) }
}

//비밀번호 확인
fun String.isPasswordValid(password: String): Boolean {
    return this == password
}

fun htmlFormat(text: String?): String {
    return text?.let {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) Html.fromHtml(it)
        else Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
    }.toString()
}

fun getListAdMobId(context: Context): String {
    return if (BuildConfig.DEBUG) context.getString(R.string.ad_mob_native_debug_id)
    else context.getString(R.string.ad_mob_list_native_release_id)
}

fun getSearchBannerAdMobId(context: Context): String {
    return if (BuildConfig.DEBUG) context.getString(R.string.ad_mob_banner_debug_id)
    else context.getString(R.string.ad_mob_search_banner_release_id)
}
fun getDetailBannerAdMobId(context: Context): String {
    return if (BuildConfig.DEBUG) context.getString(R.string.ad_mob_banner_debug_id)
    else context.getString(R.string.ad_mob_detail_banner_release_id)
}
