package com.like.drive.motorfeed.ui.base.etc

interface PagingCallback {
    fun requestMoreList()
    fun isRequest(): Boolean
}