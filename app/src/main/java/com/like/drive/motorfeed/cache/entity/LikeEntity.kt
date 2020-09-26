package com.like.drive.motorfeed.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikeEntity(
    @PrimaryKey(autoGenerate = true)
    val mID: Int?=null,
    val bid: String? = null
)