package com.like.drive.motorfeed.remote.api.feed

import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.feed.FeedData

interface FeedApi{
   suspend fun addFeed(feedData: FeedData): ResultState<Boolean>
}