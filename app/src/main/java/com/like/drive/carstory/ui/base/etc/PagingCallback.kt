package com.like.drive.carstory.ui.base.etc

interface PagingCallback {
    fun requestMoreList()
    fun isRequest(): Boolean
}