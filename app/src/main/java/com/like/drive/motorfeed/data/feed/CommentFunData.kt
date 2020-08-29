package com.like.drive.motorfeed.data.feed

data class CommentFunData(
    val comment: String,
    val fid: String,
    val type: Int = 0,
    val uid: String,
    val token: String
){
    fun getHashMap() =
        mapOf("message" to comment, "fid" to fid,"type" to type,"uid" to uid ,"token" to token)
}